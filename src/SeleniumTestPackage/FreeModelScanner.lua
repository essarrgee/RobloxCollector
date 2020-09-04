local InsertService = game:GetService("InsertService");
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
	"Spread", "heat",
	"require", "getfenv", "tonumber", "loadstring", "gsub", 
	"httpservice", 
	"reverse",
	"load", 
	"worm",
	"RotateP", "RotateV", "suka", "hack", "lolz",
	"insertNoobHere", "PhilosiphalLocations", --ProperGr�mmerNeededInPhilosiphalLocations
	"Synapse", "SynapseXen", 
	"TeleportService", "MarketplaceService", "InsertService",
	"Anti-Lag", "antiexploit", "antibackdoor", 
	"virus", "infect",
	"IronBrew", --Obfuscation
	"1000000", --Used in spread fire scripts
	"J0HN", "haxor",  --J0HNSCR1PT
	"SEX", "HAAXX",
	"CXdrU>SGS?OBQOS", --"Crash" script
	"kick", "ban", "crash", "shutdown",
	"fuck", "shit", "bitch", 
	"do not",
	"IsStudio", "IsClient", "IsServer", "IsEdit", "IsRunMode",
	"obesity", "communism", "positivity", "IEndorseThese", "crex",
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
		if (currentId) then
			local newModel = Instance.new("Model", newFolder);
			local insert = nil;
			local status, error = 
				pcall(function() insert = InsertService:LoadAsset(currentId) end);
			print("spawning "..i..": "..currentId.."... ("..assetList[i]..")");
			if (not status) then
				print("Could not spawn model ("..i..": "..currentId.."): "..error);
			end
			newModel.Name = i..": "..currentId;
			if (insert) then
				insert.Parent = newModel;
				CheckForScripts(newModel, i, currentId, newFolderServerStorage);
			end
			wait();
		end
		if (timer) then
			wait(timer);
		end
	end
	print("done.");
end

function CheckForScripts(freemodel, index, id, folder)
	if (freemodel and folder) then
		local scriptCount = 0;
		local newFolder = Instance.new("Folder", folder);
		newFolder.Name = freemodel.Name;
		for i, v in pairs(freemodel:GetDescendants()) do
			local unsafe, scriptAdd = CheckObjectSafe(freemodel, v, index, id, newFolder);
			scriptCount = scriptCount + scriptAdd;
			if (unsafe) then
				warn("Object "..v.Name.." ("..v.ClassName..")".." found under "
				..v.Parent.Name.." for model "..i..": "..freemodel.Name);
				--wait();
			end
		end
		print("Found "..scriptCount.." script(s) in "..index..": "..id);
	end
end

function CheckObjectSafe(freemodel, object, index, id, newFolder)
	local unsafe = false;
	local scriptAdd = 0;
	if (object) then
		if (object:IsA("BaseScript") or object:IsA("ModuleScript")) then
			local foundList = {};
			--print("Script "..object.Name.." found under "..object.Parent.Name.." for model "..freemodel.Name);
			if (object:IsA("BaseScript")) then
				object.Disabled = true;
			end
			--
			for i=1, #badScriptLineList do --Look through script to find bad keywords
				if (string.match(string.lower(object.Source), string.lower(badScriptLineList[i]))) then
					table.insert(foundList, badScriptLineList[i]);
				end
			end
			if (#foundList > 0) then
				warn("("..table.concat(foundList, ", ")..") found in "..object.Name.." in "..index..": "..id);
			end
			pcall(function() object:Clone().Parent = newFolder end);
			scriptAdd = 1;
		end
		if (badNameSet[object.Name] or badClassSet[object.ClassName]) then --Check by name/class
			unsafe = true;
		end
	end
	return unsafe, scriptAdd;
end

--Insert links here, surround each with quotations and separate by comma
local assetList = { 

--0!replace

}

SpawnModels(assetList);