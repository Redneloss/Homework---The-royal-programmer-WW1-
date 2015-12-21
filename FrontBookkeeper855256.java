/*
* public String updateFront(String[] array) will return null,
* and output is performed by display functions themselves.
*/
package frontbookkeeper855256;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Tikhoglo
 */
public class FrontBookkeeper855256 implements IFrontBookkeeper {
    
    public FrontBookkeeper855256() {
        this.attachedTo = new HashMap<>();
        this.soldiers = new HashMap<>();
        this.units = new HashMap<>();   
    }
    
    @Override
    public String updateFront(String[] array) {
        for (int i = 0; i < array.length; i++) {
            String news = array[i];
            String[] splitted = news.split(" ");

            if (splitted[1].equals("=")) {
                createUnit(news);
            } else if(splitted[1].equals("attached")){
                attachUnit(splitted);
            } else if(splitted[0].equals("soldiers")){
                die(splitted);
            } else if(splitted[0].equals("show") && !splitted[1].equals("soldier")){
                displaySoldiers(splitted[1]);
            } else{
                displayUnits(splitted[2]);
            }
        }
        
        return null;
    }
    
    private void createUnit(String news){
        String[] splitted = news.split(", ");
        String unit_name = splitted[0].split(" ")[0];
        
        List<Integer> soldiersForUnit = new ArrayList<>();
        this.soldiers.put(unit_name, soldiersForUnit);
        
        
        if (splitted.length > 1 ){        
            splitted[0] = splitted[0].split("\\[")[1];
            splitted[splitted.length-1] = splitted[splitted.length-1].split("\\]")[0];

            for (int i = 0; i < splitted.length; i++) {
                List<String> unitsForSoldier = new ArrayList<>();
                unitsForSoldier.add(unit_name);
                Integer soldier = Integer.parseInt(splitted[i]);
                soldiersForUnit.add(soldier);
                this.units.put(soldier, unitsForSoldier);
            }
        } else if(!news.split(" ")[2].equals("[]")){
                List<String> unitsForSoldier = new ArrayList<>();
                unitsForSoldier.add(unit_name);

                Integer soldier = Integer.parseInt(news.split("\\[")[1].split("\\]")[0]);

                soldiersForUnit.add(soldier);
                this.units.put(soldier, unitsForSoldier);
        }
    }
    
    private void attachUnit(String[] splitted) {
        String unit1_name = splitted[0];
        String unit2_name = splitted[3];
        List<Integer> unit1_soldiers = soldiers.get(unit1_name);
        List<Integer> unit2_soldiers = soldiers.get(unit2_name);
        
        String unit1_prev_attachedTo = this.attachedTo.get(unit1_name);
        
        if(splitted.length == 7){
            unit2_soldiers.addAll(unit2_soldiers.indexOf(Integer.parseInt(splitted[6]))+1, unit1_soldiers);
        } else if(splitted.length == 4){
            unit2_soldiers.addAll(unit1_soldiers);
        } 
        
        if(unit1_prev_attachedTo != null){
            for (Integer unit1_sold : unit1_soldiers) {
                this.units.get(unit1_sold).remove(unit1_prev_attachedTo);
                this.soldiers.get(unit1_prev_attachedTo).remove(unit1_sold);
            }
        }
        
        this.attachedTo.put(unit1_name, unit2_name);
        
        if (!this.soldiers.get(unit1_name).isEmpty()) {
            for (int i = 0; i < this.soldiers.get(unit1_name).size(); i++) {
                this.units.get(unit1_soldiers.get(i)).add(unit2_name); 
            }  
        }

    }
    
    private void die(String[] splitted) {
        String unit_name = splitted[3];
        List<Integer> unit_solds = soldiers.get(unit_name);
        // temp = [<s1>, <s2>]
        String[] temp = splitted[1].split("\\.\\.");
        int first = Integer.parseInt(temp[0]);
        int last = Integer.parseInt(temp[1]);
        
        int i = unit_solds.indexOf(first);
        Integer temp_soldier;
        do {
            temp_soldier = unit_solds.get(i);
            List<String> units_of_temp = this.units.get(temp_soldier);
            for (int j = 0; j < units_of_temp.size(); j++)
                this.soldiers.get(units_of_temp.get(j)).remove(temp_soldier);
            this.units.remove(temp_soldier);
            
        } while (temp_soldier != last);
    }
    
    private void displayUnits(String id) {
        int soldier_id = Integer.parseInt(id);
        if (this.units.containsKey(soldier_id)) {
            for (int i = 0; i < this.units.get(soldier_id).size(); i++) {
                System.out.print(this.units.get(soldier_id).get(i));
                if (i != this.units.get(soldier_id).size()-1) {
                    System.out.print(", ");
                }else {
                    System.out.print("\n");
                }
            }
        }
    }
    
    private void displaySoldiers(String unit_name) {
        System.out.println(this.soldiers.get(unit_name));
    }
    
    Map<String, List<Integer>> soldiers;
    Map<Integer, List<String>> units;
    Map<String, String> attachedTo;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        FrontBookkeeper855256 bk = new FrontBookkeeper855256();

        String[] tasks = {      "regiment_Stoykov = [1, 2, 3]",
                                "show regiment_Stoykov",
                                "regiment_Chaushev = [13]",
                                "show soldier 13",
                                "division_Dimitroff = []",
                                "regiment_Stoykov attached to division_Dimitroff",
                                "regiment_Chaushev attached to division_Dimitroff",
                                "show division_Dimitroff",
                                "show soldier 13",
                                "brigade_Ignatov = []",
                                "regiment_Stoykov attached to brigade_Ignatov",
                                "regiment_Chaushev attached to brigade_Ignatov after soldier 3",
                                "show brigade_Ignatov",
                                "show division_Dimitroff",
                                "brigade_Ignatov attached to division_Dimitroff",
                                "show division_Dimitroff",
                                "soldiers 2..3 from division_Dimitroff died heroically",
                                "show regiment_Stoykov",
                                "show brigade_Ignatov",
                                "show division_Dimitroff"  };
        
        bk.updateFront(tasks);
    }
}