local InsertService = game:GetService("InsertService");
local visitedItemSet = {}; --Stores item ids that have been visited already to 
						   --avoid repeats in a single session
local itemQueue = {}; --Stores every item until length reaches threshold,
					  --which it then starts deleting the oldest models in order
local itemQueueThreshold = 20;
local currentIndex = 0;

local provideMarketplaceInfo = true;
local provideInfoOutputScript = true;

--Dictionary that stores each item ID and marks them as "suspicious" if suspicious
local itemInfoMap = {};
--index, flagged, {created date, updated date, creator id, creator name}, availability

local endorsedUserIdSet = { --Users in this list are safe from flagging
	["1"] = true,
	
}

local badNameSet = { --Dictionary to store bad words
	["Spread"] = true,
	["Anti-Lag"] = true,
	["Vaccine"] = true,
	["4D Being"] = true,
	["INfecTION"] = true,
	["Infected"] = true,
	["WOMP WOMP INFECTED"] = true,
	["mean774"] = true,
	["J0HN"] = true,
	["J0HNSCR1PT"] = true,
	["Guest_Talking_Script"] = true,
	["OH SNAP YOU GOT INFECTED XD XD XD"] = true,
	["Script......Or is it..."] = true,
	["StudSurface"] = true, 
}
local badClassSet = { --Dictionary to store questionable object types
	["RotateP"] = true,
	["RotateV"] = true, 
	["VelocityMotor"] = true,
	["Feature"] = true,
	["Geometry"] = true,
	["Timer"] = true,
	["Glue"] = true,
	["Hint"] = true,
}
local badScriptLineList = {
	--POTENTIALLY suspicious keywords: 
	"require", --Could be used to call an external module, but may not always be bad
	"TeleportService", --May teleport user to another place
	"MarketplaceService", "PromptPurchase", --May be used to spam user with purchase prompts
	"InsertService", --May insert a bad model
	"IsStudio", "IsClient", "IsServer", "IsEdit", "IsRunMode", --Used by plugins; suspicious in models
	"tonumber", "gsub", "reverse", "load", "byte", --May be used to hide external module ID
	"kick", "ban", "crash", "shutdown", --May be OK if used in admin script
	"do not", --May be used in context: "DO NOT DELETE" which is suspicious
	"Anti-Lag", "antiexploit", "antibackdoor", --May be used for "anti-..." scripts 
	"Spread", "heat", --May be used in fire spread scripts, but could also be used in a non-harmful context
	"worm", "virus", "infect", "lag", "infinite", --Potentially found in "virus" scripts
	"httpservice", 
	"discord",
	
	--VERY suspicious keywords
	"getfenv", "loadstring", --Typically shouldn't use at all; used to hide an external module ID
	"RotateP", "RotateV", --Deprecated objects
	"insertNoobHere", "PhilosiphalLocations", --ProperGr�mmerNeededInPhilosiphalLocations
	"Synapse", "SynapseXen", --Synapse exploit
	"Knox", "xylem", "votation", "longicate", "ramno", "zonsa", "gibite", --"Anti-Lag"
	"IronBrew", --Obfuscation
	"1000000", --Used in spread fire scripts
	"J0HN", "haxor",  --J0HNSCR1PT
	"CXdrU>SGS?OBQOS", --"Crash" script
	"SEX", "fuck", "shit", "bitch", "fauck", --Swear words
	"obesity", "communism", "positivity", "IEndorseThese", "crex", --Was found in some exploit scripts
	"suka", "zacksisk", "cubiclemon rulz",
	"hack", "lolz", "HAAXX", "obfuscation",
	
	--Bad Module IDs
	"1398224164", 
	"1303852485", --Not too sure
	"2342106098",
	"2323876650", --Found in https://www.roblox.com/library/2604259058
	"4965769761",
	"5114417899",
	"01546813029",
	"3667797501",
	"3664252382",
	"3095323008", --IronBrew
	"3068230330",
	"5084762641",
	"4696605318",
	"3114582642",
	"4593408411", "2813844247", "3472854229", --CobaltPlus
}

function SpawnModels(assetList, timer)
	local newFolder = Instance.new("Folder", workspace);
	newFolder.Name = 
		os.date("*t")["month"].."/"..os.date("*t")["day"].." - "..os.date("*t")["hour"]..":"..os.date("*t")["min"]..":"..os.date("*t")["sec"];
	local newFolderServerStorage = newFolder:Clone();
	newFolderServerStorage.Parent = game.ServerStorage;
	for i=1, #assetList do
		local currentId = tonumber(string.match(assetList[i], '%d+'));
		if (currentId and not visitedItemSet[currentId]) then
		
			visitedItemSet[currentId] = true; --Store into visited set
			currentIndex = currentIndex + 1;
			itemInfoMap[currentId] = {currentIndex,false,{"","","",""},true};
			
			local newModel = Instance.new("Model", newFolder);
			local insert = nil;
			local status, error = 
				pcall(function() insert = InsertService:LoadAsset(currentId) end);
			print("spawning "..currentIndex..": "..currentId.."... ("..assetList[i]..")");
			if (not status) then
				warn("Could not spawn model ("..currentIndex..": "..currentId.."): "..error);
				itemInfoMap[currentId][4] = false;
			end
			newModel.Name = currentIndex..": "..currentId;
			if (insert) then
				insert.Parent = newModel;
				CheckForScripts(newModel, currentIndex, currentId, newFolderServerStorage);
			end
			CheckMarketplaceInfo(currentIndex, currentId, insert);
			AddToItemQueue(newModel);
			ManageItemQueue();
			wait();
		end
		if (timer) then
			wait(timer);
		end
	end
	PrintInfo();
	print("done.");
end

function CheckMarketplaceInfo(index, id, insert)
	if (provideMarketplaceInfo and index and id) then
		local info = nil;
		local status, error = 
			pcall(function() 
				info = game:GetService("MarketplaceService"):GetProductInfo(id);
			end);
		--index, flagged, created date, updated date, creator id, creator name
		if (info) then
			itemInfoMap[id][3][1] = info.Created;
			itemInfoMap[id][3][2] = info.Updated;
			itemInfoMap[id][3][3] = info.Creator.Id;
			itemInfoMap[id][3][4] = info.Creator.Name;
		end
	end
end

function CheckForScripts(freemodel, index, id, folder)
	if (freemodel and folder) then
		local flagged = false;
		local scriptCount = 0;
		local newFolder = Instance.new("Folder", folder);
		newFolder.Name = freemodel.Name;
		for i, v in pairs(freemodel:GetDescendants()) do
			local _flagged, unsafe, scriptAdd = CheckObjectSafe(freemodel, v, index, id, newFolder);
			scriptCount = scriptCount + scriptAdd;
			if (unsafe) then
				warn("Object "..v.Name.." ("..v.ClassName..")".." found under "
				..v.Parent.Name.." for model "..i..": "..freemodel.Name);
				--wait();
			end
			if (_flagged or unsafe) then
				flagged = true;
			end
		end
		if (flagged) then
			itemInfoMap[id][2] = true;
		end
		print("Found "..scriptCount.." script(s) in "..index..": "..id);
	end
end

function CheckObjectSafe(freemodel, object, index, id, newFolder)
	local flagged = false;
	local unsafe = false;
	local scriptAdd = 0;
	if (object) then
		if (object:IsA("BaseScript") or object:IsA("ModuleScript")) then
			local foundList = {};
			--print("Script "..object.Name.." found under "..object.Parent.Name.." for model "..freemodel.Name);
			--
			for i=1, #badScriptLineList do --Look through script to find bad keywords
				if (string.match(string.lower(object.Source), string.lower(badScriptLineList[i]))) then
					table.insert(foundList, badScriptLineList[i]);
				end
			end
			if (#foundList > 0) then
				flagged = true;
				warn("("..table.concat(foundList, ", ")..") found in "..object.Name.." in "..index..": "..id);
			end
			pcall(function() 
				local clone = object:Clone();
				if (clone:IsA("BaseScript")) then
					clone.Disabled = true;
				end
				clone.Parent = newFolder;
			end);
			scriptAdd = 1;
		end
		if (badNameSet[object.Name] or badClassSet[object.ClassName]) then --Check by name/class
			unsafe = true;
		end
	end
	return flagged, unsafe, scriptAdd;
end

function PrintInfo()
	if (provideInfoOutputScript) then
		for i,v in pairs(game.Players:GetChildren()) do
			if (v.Name == "Output") then
				v:Destroy();
			end
		end
		local outputScript = Instance.new("Script", game.Players);
		outputScript.Name = "Output";
		for i,v in pairs(itemInfoMap) do
			local newEntry = ""
			print(v[1]..": "..i ..":", v[2]);
			--Use "::" to separate each sub-index to not interfere with the time
			--Use "," to separate each entry
			newEntry = newEntry..(i.."::");
			if (v[3] and v[3][3] and endorsedUserIdSet[tostring(v[3][3])]) then
				warn(v[1]..": is endorsed");
				newEntry = newEntry.."NOT_FLAGGED::";
			elseif (v[2]) then
				newEntry = newEntry.."FLAGGED::";
			else
				newEntry = newEntry.."NOT_FLAGGED::";
			end
			if (v[3]) then
				for i=1, 4 do
					if (v[3][i] and v[3][i] ~= "") then
						newEntry = newEntry..(tostring(v[3][i])).."::";
					else
						newEntry = newEntry.."nil::";
					end
				end
			end
			if (v[4]) then
				newEntry = newEntry.."true::";
			else
				warn(v[1]..": is unavailable");
				newEntry = newEntry.."false::";
			end
			outputScript.Source = newEntry..","
		end
		print("Output script created! Find in game.Players");
	end
end

function AddToItemQueue(item)
	if (item) then
		table.insert(itemQueue, item);
	end
end

function ManageItemQueue()
	local nextItem = nil;
	if (#itemQueue > itemQueueThreshold) then
		nextItem = table.remove(itemQueue, 1);
		if (nextItem) then
			nextItem:Destroy();
		end
	end
end


--Insert links here, surround each with quotations and separate by comma
local assetList = { 

--0!replace

}

SpawnModels(assetList);