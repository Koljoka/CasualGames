package com.example.casualgames.service;

import com.example.casualgames.entity.Sport;
import com.example.casualgames.repository.SportRepository;

import org.springframework.stereotype.Service;

import java.util.List;


//Service kerros Sport logiikkaa varten
@Service
public class SportService {

    //Repository tietokantaoperaatioita varten
    private final SportRepository sportRepository;

    //Konstruktori injektoi repositoryn
    public SportService(SportRepository sportRepository) {
        this.sportRepository = sportRepository;
    }

    //Hae kaikki lajit
    public List<Sport> findAllSports() {
        return sportRepository.findAll();
    }
}