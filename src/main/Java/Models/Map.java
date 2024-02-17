package Models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import Exceptions.InvalidMap;
import Utils.CommonUtil;
import java.util.Collections;
import java.util.Map.Entry;

public class Map {
    String d_mapFile;

    List<Continent> d_continents;

    List<Country> d_countries;

    HashMap<Integer, Boolean> d_countryReach = new HashMap<Integer, Boolean>();

    public String getD_mapFile() {
        return d_mapFile;
    }

    public void setD_mapFile(String p_mapFile) {
        this.d_mapFile = p_mapFile;
    }

    public List<Continent> getD_continents() {
        return d_continents;
    }

    public void setD_continents(List<Continent> p_continents) {
        this.d_continents = p_continents;
    }

    public List<Country> getD_countries() {
        return d_countries;
    }

    public void setD_countries(List<Country> p_countries) {
        this.d_countries = p_countries;
    }

    public void addContinent(Continent p_continent){
        d_continents.add(p_continent);
    }
    public void addCountry(Country p_country){
        d_countries.add(p_country);
    }

    public List<Integer> getContinentIDs(){
        List<Integer> l_continentIDs = new ArrayList<Integer>();
        if (!d_continents.isEmpty()) {
            for(Continent c: d_continents){
                l_continentIDs.add(c.getD_continentID());
            }
        }
        return l_continentIDs;
    }

    public List<Integer> getCountryIDs(){
        List<Integer> l_countryIDs = new ArrayList<Integer>();
        if(!d_countries.isEmpty()){
            for(Country c: d_countries){
                l_countryIDs.add(c.getD_countryId());
            }
        }
        return l_countryIDs;
    }

    public void checkContinents() {
        for(Continent c: d_continents) {
            System.out.println(c.getD_continentID());
        }
    }

    public void checkCountries() {
        for (Country c: d_countries) {
            System.out.println("Country Id "+ c.getD_countryId());
            System.out.println("Continent Id "+c.getD_continentId());
            System.out.println("Neighbours:");
            for (int i: c.getD_adjacentCountryIds()) {
                System.out.println(i);
            }
        }
    }

    public Boolean Validate() throws InvalidMap {
        return (!checkForNullObjects() && checkContinentConnectivity() && checkCountryConnectivity());
    }

    public Boolean checkForNullObjects() throws InvalidMap{
        if(d_continents==null || d_continents.isEmpty()){
            throw new InvalidMap("Map must possess atleast one continent!");
        }
        if(d_countries==null || d_countries.isEmpty()){
            throw new InvalidMap("Map must possess atleast one country!");
        }
        for(Country c: d_countries){
            if(c.getD_adjacentCountryIds().size()<1){
                throw new InvalidMap(c.getD_countryName()+" does not possess any neighbour, hence isn't reachable!");
            }
        }
        return false;
    }

    public Boolean checkContinentConnectivity() throws InvalidMap {
        boolean l_flagConnectivity=true;
        for (Continent c:d_continents){
            if (null == c.getD_countries() || c.getD_countries().size()<1){
                throw new InvalidMap(c.getD_continentName() + " has no countries, it must possess atleast 1 country");
            }
            if(!subGraphConnectivity(c)){
                l_flagConnectivity=false;
            }
        }
        return l_flagConnectivity;
    }

    public boolean subGraphConnectivity(Continent p_continent) throws InvalidMap {
        HashMap<Integer, Boolean> l_continentCountry = new HashMap<Integer, Boolean>();

        for (Country c : p_continent.getD_countries()) {
            l_continentCountry.put(c.getD_countryId(), false);
        }
        dfsSubgraph(p_continent.getD_countries().get(0), l_continentCountry, p_continent);

        // Iterates Over Entries to locate unreachable countries in continent
        for (Entry<Integer, Boolean> entry : l_continentCountry.entrySet()) {
            if (!entry.getValue()) {
                Country l_country = getCountry(entry.getKey());
                String l_messageException = l_country.getD_countryName() + " in Continent " + p_continent.getD_continentName() + " is not reachable";
                throw new InvalidMap(l_messageException);
            }
        }
        return !l_continentCountry.containsValue(false);
    }

    public void dfsSubgraph(Country p_c, HashMap<Integer, Boolean> p_continentCountry, Continent p_continent) {
        p_continentCountry.put(p_c.getD_countryId(), true);
        for (Country c : p_continent.getD_countries()) {
            if (p_c.getD_adjacentCountryIds().contains(c.getD_countryId())) {
                if (!p_continentCountry.get(c.getD_countryId())) {
                    dfsSubgraph(c, p_continentCountry, p_continent);
                }
            }
        }
    }

    public void dfsCountry(Country p_c) throws InvalidMap {
        d_countryReach.put(p_c.getD_countryId(), true);
        for (Country l_nextCountry : getAdjacentCountry(p_c)) {
            if (!d_countryReach.get(l_nextCountry.getD_countryId())) {
                dfsCountry(l_nextCountry);
            }
        }
    }

    public boolean checkCountryConnectivity() throws InvalidMap {
        for (Country c : d_countries) {
            d_countryReach.put(c.getD_countryId(), false);
        }
        dfsCountry(d_countries.get(0));

        // Iterates over entries to locate the unreachable country
        for (Entry<Integer, Boolean> entry : d_countryReach.entrySet()) {
            if (!entry.getValue()) {
                String l_exceptionMessage = getCountry(entry.getKey()).getD_countryName() + " country is not reachable";
                throw new InvalidMap(l_exceptionMessage);
            }
        }
        return !d_countryReach.containsValue(false);
    }


    public List<Country> getAdjacentCountry(Country p_country) throws InvalidMap {
        List<Country> l_adjCountries = new ArrayList<Country>();

        if (p_country.getD_adjacentCountryIds().size() > 0) {
            for (int i : p_country.getD_adjacentCountryIds()) {
                l_adjCountries.add(getCountry(i));
            }
        } else {
            throw new InvalidMap(p_country.getD_countryName() + " doesn't have any adjacent countries");
        }
        return l_adjCountries;
    }

    public Country getCountry(Integer p_countryId) {
        return d_countries.stream().filter(l_country -> l_country.getD_countryId().equals(p_countryId)).findFirst().orElse(null);
    }

    public Country getCountryByName(String p_countryName){
        return d_countries.stream().filter(l_country -> l_country.getD_countryName().equals(p_countryName)).findFirst().orElse(null);
    }

    public Continent getContinent(String p_continentName){
        return d_continents.stream().filter(l_continent -> l_continent.getD_continentName().equals(p_continentName)).findFirst().orElse(null);
    }

    public Continent getContinentByID(Integer p_continentID){
        return d_continents.stream().filter(l_continent -> l_continent.getD_continentID().equals(p_continentID)).findFirst().orElse(null);
    }

    public void addContinent(String p_continentName, Integer p_controlValue) throws InvalidMap{
        int l_continentId;

        if (d_continents!=null) {
            l_continentId=d_continents.size()>0?Collections.max(getContinentIDs())+1:1;
            if(CommonUtil.isNull(getContinent(p_continentName))){
                d_continents.add(new Continent(l_continentId, p_continentName, p_controlValue));
            }else{
                throw new InvalidMap("Continent cannot be added! It already exists!");
            }
        }else{
            d_continents= new ArrayList<Continent>();
            d_continents.add(new Continent(1, p_continentName, p_controlValue));
        }
    }

    public void removeContinent(String p_continentName) throws InvalidMap{
        if (d_continents!=null) {
            if(!CommonUtil.isNull(getContinent(p_continentName))){

                // Deletes the continent and updates neighbour as well as country objects
                if (getContinent(p_continentName).getD_countries()!=null) {
                    for(Country c: getContinent(p_continentName).getD_countries()){
                        removeCountryNeighboursFromAll(c.getD_countryId());
                        updateNeighboursCont(c.getD_countryId());
                        d_countries.remove(c);
                    }
                }
                d_continents.remove(getContinent(p_continentName));
            }else{
                throw new InvalidMap("No such Continent exists!");
            }
        } else{
            throw new InvalidMap("No continents in the Map to remove!");
        }
    }

    public void addCountry(String p_countryName, String p_continentName) throws InvalidMap{
        int l_countryId;
        if(d_countries==null){
            d_countries= new ArrayList<Country>();
        }
        if(CommonUtil.isNull(getCountryByName(p_countryName))){
            l_countryId=d_countries.size()>0? Collections.max(getCountryIDs())+1:1;
            if(d_continents!=null && getContinentIDs().contains(getContinent(p_continentName).getD_continentID())){
                Country l_country= new Country(l_countryId, p_countryName, getContinent(p_continentName).getD_continentID());
                d_countries.add(l_country);
                for (Continent c: d_continents) {
                    if (c.getD_continentName().equals(p_continentName)) {
                        c.addCountry(l_country);
                    }
                }
            } else{
                throw new InvalidMap("Cannot add Country to a Continent that doesn't exist!");
            }
        }else{
            throw new InvalidMap("Country with name "+ p_countryName+" already Exists!");
        }
    }

    public void removeCountry(String p_countryName) throws InvalidMap{
        if(d_countries!=null && !CommonUtil.isNull(getCountryByName(p_countryName))) {
            for(Continent c: d_continents){
                if(c.getD_continentID().equals(getCountryByName(p_countryName).getD_continentId())){
                    c.removeCountry(getCountryByName(p_countryName));
                }
                c.removeCountryNeighboursFromAll(getCountryByName(p_countryName).getD_countryId());
            }
            removeCountryNeighboursFromAll(getCountryByName(p_countryName).getD_countryId());
            d_countries.remove(getCountryByName(p_countryName));

        }else{
            throw new InvalidMap("Country:  "+ p_countryName+" does not exist!");
        }
    }

    public void addCountryNeighbour(String p_countryName, String p_neighbourName) throws InvalidMap{
        if(d_countries!=null){
            if(!CommonUtil.isNull(getCountryByName(p_countryName)) && !CommonUtil.isNull(getCountryByName(p_neighbourName))){
                d_countries.get(d_countries.indexOf(getCountryByName(p_countryName))).addNeighbour(getCountryByName(p_neighbourName).getD_countryId());
            } else{
                throw new InvalidMap("Invalid Neighbour Pair! Either of the Countries Doesn't exist!");
            }
        }
    }

    public void removeCountryNeighboursFromAll(Integer p_countryID){
        for (Country c: d_countries) {
            if (!CommonUtil.isNull(c.getD_adjacentCountryIds())) {
                if (c.getD_adjacentCountryIds().contains(p_countryID)) {
                    c.removeNeighbour(p_countryID);
                }
            }
        }
    }

    public void removeCountryNeighbour(String p_countryName, String p_neighbourName) throws InvalidMap{
        if(d_countries!=null){
            if(!CommonUtil.isNull(getCountryByName(p_countryName)) && !CommonUtil.isNull(getCountryByName(p_neighbourName))) {
                d_countries.get(d_countries.indexOf(getCountryByName(p_countryName))).removeNeighbour(getCountryByName(p_neighbourName).getD_countryId());
            } else{
                throw new InvalidMap("Invalid Neighbour Pair! Either of the Countries Doesn't exist!");
            }
        }
    }

    public void updateNeighboursCont(Integer p_countryId){
        for(Continent c: d_continents){
            c.removeCountryNeighboursFromAll(p_countryId);
        }
    }

}
