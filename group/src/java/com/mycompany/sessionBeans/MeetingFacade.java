/*
 * Created by Alex Martin on 2017.04.11  * 
 * Copyright © 2017 Alex Martin. All rights reserved. * 
 */
package com.mycompany.sessionBeans;

import com.mycompany.entityClasses.Meeting;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author alexmartin
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
