/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package devs.mrp.turkeydesktop.service.resourcehandler;

/**
 *
 * @author ncm55070
 */
public interface ResourceHandler<TYPE,NAME> {
    public TYPE getResource(NAME name);
}
