package de.viadee.parkhaus.schranke.service;

import de.viadee.parkhaus.schranke.api.ParkhausManager;

import javax.inject.Singleton;

@Singleton
public class SchrankeService {

    ParkhausManager parkhausManager;

    public SchrankeService(ParkhausManager parkhausManager) {
        this.parkhausManager = parkhausManager;
    }

    public boolean canExit(String parkhausId) {
        return this.parkhausManager.isAllowedToExit(parkhausId);
    }

}
