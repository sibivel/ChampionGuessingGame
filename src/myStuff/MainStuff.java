package myStuff;

import java.util.Arrays;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import util.Util;


public class MainStuff {
	
	private int participantId = 5;
	
	private String key =  "?api_key="+ util.Config.RIOT_API_KEY;
	private String naURL  = "https://na.api.pvp.net/api/lol/na";
	private String naURL2  = "https://na.api.pvp.net/api/lol/static-data/na";//used for static data like champion name.
	private String summonerByName = "/v1.4/summoner/by-name/";
	private String matchList = "/v2.2/matchlist/by-summoner/";
	private String matchURL = "/v2.2/match/";
	private String champURL ="/v1.2/champion"; //takes champ id at the end. could be replaced so that all the champ data is saved locally.
	private String masteryURL = "/v1.2/mastery";
	private String spellURL = "/v1.2/summoner-spell";
	private String itemsURL = "/v1.2/item";
	
	//ddragon urls:
	public final String itemImage = "http://ddragon.leagueoflegends.com/cdn/7.3.1/img/item/";//[id].png
	public final String masteryImage = "http://ddragon.leagueoflegends.com/cdn/7.3.1/img/mastery/";//[id].png
	public final String spellImage = "http://ddragon.leagueoflegends.com/cdn/7.3.1/img/spell/";//name.png
	public final String champImage = "http://ddragon.leagueoflegends.com/cdn/7.3.2/img/champion/";//name.png
	
	private JSONObject masteryData;
	private JSONObject spellData;
	private JSONObject itemData;
	private JSONObject champData;
	
	//match specific data variables
	private JSONObject match;
	private JSONObject participant;
	private JSONObject stats;
	private int[] items;
	private String[] itemNames;
	private int champId;
	private String champName;
	private int keystoneId;
	private String keystoneName;
	private int[] kda;
	private int cs;
	private int firstBlood;
	private int[] spellIds;
	private String[] spellNames;
	private String[] spellKeys;
	private int[] skillLevelUpOrder;
	private String role;
	private int[] champOptions;
	private String[] optionKeys;
	private String[] optionNames;
	
	public MainStuff() {
		try {
			masteryData = Util.sendGet(naURL2 + masteryURL + key).getJSONObject("data");
			JSONObject data = new JSONObject();//should just have strings
			data.accumulate("dataById", true);
			spellData = Util.sendGet(naURL2 + spellURL + key, data).getJSONObject("data");
			itemData = Util.sendGet(naURL2 + itemsURL + key).getJSONObject("data");
			champData = Util.sendGet(naURL2 + champURL + key,data).getJSONObject("data");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
	}

	public static void main(String args[]) throws Exception{
		MainStuff main = new MainStuff();
		main.getInfo("2397222176",5);
	}
	
	public void getInfo(String matchId, int participantId) throws Exception{
		//matchId = "2397222176";
		
		match = getMatch(matchId);
		participant = getParticipant(match,participantId);
		stats = getStats(participant);
		items = getItems(stats);
		itemNames = getItemNames(items);
		
		champId = getChampId(participant);
		champName = getChampName(champId);
		
		keystoneId = getKeystoneId(participant);
		keystoneName = getKeystoneName(keystoneId);
		
		kda = getKDA(stats);
		
		cs = getCS(stats);
		
		firstBlood = getFirstBlood(stats);
		
		spellIds = getSpellIds(participant);
		spellNames = getSpellNames(spellIds);
		spellKeys = getSpellKeys(spellIds);
		
		skillLevelUpOrder = getSkillLevelUpOrder(match);
		
		role = getRole(participant);
		
		champOptions = getChampOptions(champName);
		optionKeys = getOptionKeys(champOptions);
		optionNames = getOptionNames(champOptions);
		
		System.out.println("\n"+ "Champ Name: " + champName);
		System.out.println("\n"+ "Items: " + Arrays.toString(itemNames));
		System.out.println("\n"+ "Keystone Name: " + keystoneName);
		System.out.println("\n"+ "KDA: " + kda[0] + "/" + kda[1] + "/" + kda[2]);
		System.out.println("\nCS: " + cs);
		System.out.println("\nFirst Blood: " + firstBlood);
		System.out.println("\nSpell 1: " + spellNames[0]);
		System.out.println("Spell 2: " + spellNames[1]);
		System.out.println("\nSkill Level Up Order: "+Arrays.toString(skillLevelUpOrder));
		System.out.println("\nRole: " + role);
		System.out.println("\nOptions: " + Arrays.toString(champOptions));
		System.out.println("\nOptions: " + Arrays.toString(optionKeys));
		System.out.println("\nOptions: " + Arrays.toString(optionNames));
		
	}
	
	
	private JSONObject getMatch(String matchId) throws Exception{
		JSONObject data = new JSONObject();
		data.accumulate("includeTimeline", true);
		return Util.sendGet(naURL + matchURL + matchId + key,data);
	}
	
	private JSONObject getParticipant(JSONObject match,int participantId) throws JSONException{
		JSONObject participant;
		JSONArray participants = match.getJSONArray("participants");
		participant = participants.getJSONObject(participantId-1);//need the -1 here because array indexes from 0, but participant id does not.
				
		return participant;
	}
	
	private int getChampId(JSONObject participant) throws JSONException{
		return participant.getInt("championId");
	}
	
	private String getChampName(int id) throws Exception{
		return champData.getJSONObject(""+id).getString("name");
	}
	//takes a participant and returns their stats
	private JSONObject getStats(JSONObject participant) throws JSONException{
		return participant.getJSONObject("stats");
	}
	
	//takes stats of a participant and returns array[7] of the items 
	private int[] getItems(JSONObject stats) throws JSONException{
		int[] items = new int[7];
		for(int i = 0; i<7; i++){
			items[i] = stats.getInt("item"+i);
		}
		
		return items;
	}
	
	private String[] getItemNames(int[] items) throws JSONException{
		String[] itemNames = new String[7];
		for(int i = 0; i<items.length; i++){
			itemNames[i] = itemData.getJSONObject(""+items[i]).getString("name");
		}
		return itemNames;
	}
	
	/*It seems like all the keystone masteries have 6 as their 3rd digit, so im using that to find the keystone.
	 * 
	 */
	private int getKeystoneId(JSONObject participant) throws JSONException{
		JSONArray masteries = participant.getJSONArray("masteries");
		int keystoneId = -1;
		for(int i = 0; i<masteries.length(); i++){
			JSONObject mastery = masteries.getJSONObject(i);
			int id = mastery.getInt("masteryId");
			if((id/10) % 10 == 6){
				keystoneId = id;
			}
		}
		
		return keystoneId;
		
	}
	
	private String getKeystoneName(int id) throws Exception{
		
		return masteryData.getJSONObject(""+id).getString("name");
	}
	
	//returns array[3] with KDA
	private int[] getKDA(JSONObject stats) throws JSONException{
		int[] kda = new int[3];
		kda[0] = stats.getInt("kills");
		kda[1] = stats.getInt("deaths");
		kda[2] = stats.getInt("assists");
		return kda;
		
	}
	
	private int getCS(JSONObject stats) throws JSONException{
		int minions = stats.getInt("minionsKilled");
		int neutrals = stats.getInt("neutralMinionsKilled");
		return minions + neutrals;
	}
	
	//returns 0 for no participation, 1 for kill, 2 for assist.
	private int getFirstBlood(JSONObject stats) throws JSONException{
		if (stats.getBoolean("firstBloodKill"))
			return 1;
		else if (stats.getBoolean("firstBloodAssist"))
			return 2;
		else
			return 0;
	}
	
	//returns array[2] with summoner spell ids
	private int[] getSpellIds(JSONObject participant) throws JSONException{
		int[] spells = new int[2];
		spells[0] = participant.getInt("spell1Id");
		spells[1] = participant.getInt("spell2Id");
		return spells;
	}
	
	private String[] getSpellNames(int[] spellIds) throws JSONException{
		String[] spellNames = new String[2];
		spellNames[0] = spellData.getJSONObject(""+spellIds[0]).getString("name");
		spellNames[1] = spellData.getJSONObject(""+spellIds[1]).getString("name");
		return spellNames;
	}
	
	private String[] getSpellKeys(int[] spellIds) throws JSONException{
		String[] spellKeys = new String[2];
		spellKeys[0] = spellData.getJSONObject(""+spellIds[0]).getString("key");
		spellKeys[1] = spellData.getJSONObject(""+spellIds[1]).getString("key");
		return spellKeys;
	}
	
	//array of level ups 1 = q,2=w,3=e,4=r
	private int[] getSkillLevelUpOrder(JSONObject match) throws JSONException{
		JSONObject timeline = match.getJSONObject("timeline");
		JSONArray frames = timeline.getJSONArray("frames");
		int[] levels = new int[18];
		int level = 0;
		for(int i = 0; i < frames.length() && level < 18; i++){
			JSONObject frame = frames.getJSONObject(i);
			if(frame.has("events")){
				JSONArray events = frame.getJSONArray("events");
				for(int j = 0; j < events.length(); j++){
					JSONObject event = events.getJSONObject(j);
					if(event.has("participantId") && event.getInt("participantId") == participantId && event.getString("eventType").equals("SKILL_LEVEL_UP") && event.getString("levelUpType").equals("NORMAL")){
						levels[level] = event.getInt("skillSlot");
						level++;
					}
				}
			}
		}
		return levels;
		
	}
	
	private String getRole(JSONObject participant) throws JSONException{
		JSONObject timeline = participant.getJSONObject("timeline");
		String role = timeline.getString("role");
		String lane = timeline.getString("lane");
		if(lane.equals("BOTTOM"))
			return role;
		else
			return lane;
	}
	
	private int[] getChampOptions(String champName) throws JSONException{
		Iterator<String> iter = champData.keys();
		int[] champIDs = new int[champData.length()];
		int i = 0;
		while(iter.hasNext()){
			champIDs[i] = champData.getJSONObject(iter.next()).getInt("id");
			i++;
		}
		
		int[] options = new int[10];
		int answerLocation = (int) (Math.random() * options.length);
		options[answerLocation] = champId;
		i = 0;
		boolean repeat = false;
		while(i < options.length){
			if(i != answerLocation){
				int id = champIDs[(int) (Math.random() * champIDs.length)];
				
				//check for repeats
				for(int j = 0; j < options.length; j++){
					if(id == options[j])
						repeat = true;
				}
				if(!repeat){
					options[i] = id;
				}else{
					i--;
					repeat = false;
				}
			}
			i++;
		}
		return options;
	}
	
	private String[] getOptionKeys(int[] optionIds) throws JSONException{
		String[] keys = new String[optionIds.length];
		for(int i = 0; i<optionIds.length;i++){
			keys[i] = champData.getJSONObject(""+optionIds[i]).getString("key");
		}
		return keys;
	}
	
	private String[] getOptionNames(int[] optionIds) throws JSONException{
		String[] names = new String[optionIds.length];
		for(int i = 0; i<optionIds.length;i++){
			names[i] = champData.getJSONObject(""+optionIds[i]).getString("name");
		}
		return names;
	}
	
	
	
	
	
	public int getParticipantId() {
		return participantId;
	}

	public JSONObject getMatch() {
		return match;
	}

	public JSONObject getParticipant() {
		return participant;
	}

	public JSONObject getStats() {
		return stats;
	}

	public int[] getItems() {
		return items;
	}

	public String[] getItemNames() {
		return itemNames;
	}

	public int getChampId() {
		return champId;
	}

	public String getChampName() {
		return champName;
	}

	public int getKeystoneId() {
		return keystoneId;
	}

	public String getKeystoneName() {
		return keystoneName;
	}

	public int[] getKda() {
		return kda;
	}

	public int getCS() {
		return cs;
	}

	public int getFirstBlood() {
		return firstBlood;
	}

	public int[] getSpellIds() {
		return spellIds;
	}

	public String[] getSpellNames() {
		return spellNames;
	}
	
	public String[] getSpellKeys() {
		return spellKeys;
	}

	public int[] getSkillLevelUpOrder() {
		return skillLevelUpOrder;
	}

	public String getRole() {
		return role;
	}
	
	public int[] getChampOptions(){
		return champOptions;
	}
	
	public String[] getOptionKeys(){
		return optionKeys;
	}
	
	public String[] getOptionNames(){
		return optionNames;
	}
	
}
