package br.com.alura.screenmatch.controller;

import br.com.alura.screenmatch.dto.SerieDTO;
import br.com.alura.screenmatch.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SerieController {

    @Autowired
    private SerieRepository repository;

    @GetMapping("/series")
    public List<SerieDTO> obterSeries() {
        return repository.findAll()
                .stream()
                .map(s -> new SerieDTO(s.getId(), s.getTitulo(), s.getTotalTemporadas(), s.getAvaliacao(),
                s.getGenero(), s.getAtores(), s.getImagemPoster(), s.getSinopse())).toList();
    }

    @GetMapping("/top5")
    public List<SerieDTO> obterTop5() {
        return repository.findTop5ByOrderByAvaliacaoDesc()
                .stream()
                .map(serie -> new SerieDTO(serie.getId(), serie.getTitulo(), serie.getTotalTemporadas(), serie.getAvaliacao(),
                        serie.getGenero(), serie.getAtores(), serie.getImagemPoster(), serie.getSinopse())).toList();
    }
}
