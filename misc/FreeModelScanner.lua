local InsertService = game:GetService("InsertService");
local badNameSet = { --Dictionary to store bad words
	["Spread"] = true,
	["Anti-Lag"] = true,
	["Vaccine"] = true,
	["4D Being"] = true,
	["INfecTION"] = true,
	["WOMP WOMP INFECTED"] = true,
	["mean774"] = true,
	["J0HN"] = true,
	["J0HNSCR1PT"] = true,
	["Guest_Talking_Script"] = true,
}
local badClassSet = { --Dictionary to store questionable object types
	["RotateP"] = true,
	["Glue"] = true,
	["Hint"] = true,
}
local badScriptLineList = {
	"Spread", 
	"require", "getfenv", "tonumber", "load", "reverse",
	"Synapse", "SynapseXen", 
	"TeleportService", "MarketplaceService", "InsertService",
	"Anti-Lag",
	"virus",
	"1000000", --Used in spread fire scripts
	"J0HN", "haxor",  --J0HNSCR1PT
	"SEX", "HAAXX",
	"CXdrU>SGS?OBQOS", --"Crash" script
	"kick", "crash", "shutdown",
	--Below belongs to the "BaseConvertor" script
	"y1syd", "K2SMe7", "UFwB8_X7WW", "K41eHt", 
	"DZPKaIYZrP", "jX7pfS1", "Jm0BG7iF", "UFwB8_X7WW",
	"s24_HdvbDOm", "l8e8NA", "xcKTAo__", "DqqY",
	"DZPKaIYZrP", "d1LMl", "BqtWenAqN"
	
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
			--print("Script "..object.Name.." found under "..object.Parent.Name.." for model "..freemodel.Name);
			if (object:IsA("BaseScript")) then
				object.Disabled = true;
			end
			--
			for i=1, #badScriptLineList do --Look through script to find bad keywords
				if (string.match(string.lower(object.Source), string.lower(badScriptLineList[i]))) then
					warn(badScriptLineList[i].." found in "..object.Name.." in "..index..": "..id);
					break;
				end
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
	--Spawner
	"https://www.roblox.com/library/208356627/Spawner",
	"https://www.roblox.com/library/582148931/Spawners",
	"https://www.roblox.com/library/1339262914/Epic-Spawner",
	"https://www.roblox.com/library/3930446225/Spawner",
	"https://www.roblox.com/library/1294191967/Spawner-NOGROUP",
	"https://www.roblox.com/library/784106572/Weapon-Spawner",
	"https://www.roblox.com/library/2833017581/Shotgun-Spawner",
	"https://www.roblox.com/library/1190238599/Rifle-spawner",
	"https://www.roblox.com/library/1088315965/Uzi-Spawner",
	"https://www.roblox.com/library/137919919/Zombie-Spawner",
	"https://www.roblox.com/library/467597362/Rainbow-Spawner",
	"https://www.roblox.com/library/73057973/jeep-spawner"
	
	
	
	



}

SpawnModels(assetList);