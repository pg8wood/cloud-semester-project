/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.sessionBeans;

import com.mycompany.entityClasses.Meeting;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Jason
 */
@Stateless
public class MeetingFacade extends AbstractFacade<Meeting> {

    @PersistenceContext(unitName = "groupPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public MeetingFacade() {
        super(Meeting.class);
    }
    
}
