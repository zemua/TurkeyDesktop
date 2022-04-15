/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package devs.mrp.turkeydesktop.view.groups.review;

/**
 *
 * @author ncm55070
 */
public enum ComboOrderEnum {
        UNASSIGNED_FIRST("unassignedFirst"), ASSIGNED_HERE_FIRST("assignedHereFirst");
        
        String key;
        
        ComboOrderEnum(String key) {
            this.key = key;
        }
        
        public String getKey() {
            return this.key;
        }
        
    }
