/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package menu;

import Characters.PlayableCharacter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Inferno
 */
public class Team 
{
    Player member1;
    Player member2;
    Player member3;
//    Player[] members = {member1, member2, member3};
    Player[] members = new Player[3];
    public static List<String> teamList = new ArrayList();
    public static HashMap<String, Team> availableTeams = new HashMap();
    
    String color;
    
    public Team()
    {
        
    }
    
    public Team(String color)
    {
        this.color = color;
    }
    
    /**
     * Takes in a color name and adds a new team with that color.
     * @param color 
     */
    public static void addTeam(String color)
    {
//        Team newTeam = new Team(color);
//        Team.teamList.add(newTeam);
        Team.teamList.add(color);
        Team.availableTeams.put(color, new Team(color)); // Adds the team to the team list; which will be used by all teams
        PlayableCharacter.teamCostumes.put(color, buildTeam(color));
    }
    
    public static boolean addMember(String color, Player member)
    {
        Team team = availableTeams.get(color);
        if (team == null)
        {
            return false;
        }
        if (team.members[0] == null)
        {
            team.members[0] = member;
            return true;
        }
        else if (team.members[1] == null)
        {
            team.members[1] = member;
            return true;
        }
        else if (team.members[2] == null)
        {
            team.members[2] = member;
            return true;
        }
        return false;
    }
    
    public static boolean removeMember(String color, Player member)
    {
        Team team = availableTeams.get(color);
        if (team == null)
        {
            return false;
        }
        if (member.equals(team.members[0]))
        {
            team.members[0] = null;
            return true;
        }
        else if (member.equals(team.members[1]))
        {
            team.members[1] = null;
            return true;
        }
        else if (member.equals(team.members[2]))
        {
            team.members[2] = null;
            return true;
        }
        return false;
    }
    
    /**
     * Takes in a color name and creates the necessary costumes for a team out of it.
     * @param color
     * @return 
     */
    public static List<String> buildTeam(String color)
    {
        List<String> teamCostumes = new ArrayList(3);
        teamCostumes.add(color);
        teamCostumes.add("Light" + color);
        teamCostumes.add("Dark" + color);
//        List<Costume> teamCostumes = new ArrayList(3);
//        teamCostumes.add(new Costume(color, ""));
//        teamCostumes.add(new Costume("Light" + color, ""));
//        teamCostumes.add(new Costume("Dark" + color, ""));
        return teamCostumes;
    }
    
    public static HashMap<String, List<String>> buildTeamMap(String[] teams) 
    {
        HashMap<String, List<String>> teamMap = new HashMap();
        
        for (int i = 0; i < teams.length; i++) 
        {
            Team.teamList.add(teams[i]);
            Team.availableTeams.put(teams[i], new Team(teams[i]));
            teamMap.put(teams[i], buildTeam(teams[i]));  
        }
        
        return teamMap;
    }
    
    /**
     * Takes in a color name and checks if the associated team is full.
     * @param color
     * @return 
     */
    public static boolean teamFull(String color)
    {
        Team current = availableTeams.get(color);
        
        if (current == null)
        {
            return false;
        }
        if (current.members[0] == null)
        {
            return false;
        }
        if (current.members[1] == null)
        {
            return false;
        }
        if (current.members[2] == null)
        {
            return false;
        }
        
        return true;
    }
    
    public static String stringMembers(String color)
    {
        if (color.equals(""))
        {
            return "No team";
        }
        String memberList = "";
        Player[] memberArray = availableTeams.get(color).members;
        for (int i = 0; i < memberArray.length; i++) 
        {
            memberList += (memberArray[i] + " ");
        }
        return memberList;
    }
  

}
