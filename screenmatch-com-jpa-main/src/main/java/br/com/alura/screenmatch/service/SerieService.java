package br.com.alura.screenmatch.service;

import br.com.alura.screenmatch.dto.EpisodioDTO;
import br.com.alura.screenmatch.dto.SerieDTO;
import br.com.alura.screenmatch.model.Categoria;
import br.com.alura.screenmatch.model.Episodio;
import br.com.alura.screenmatch.model.Serie;
import br.com.alura.screenmatch.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SerieService {
    @Autowired
    SerieRepository repositorio;

    private List<SerieDTO> converterParaSerieDTO(List<Serie> series) {
        return series.stream()
                .map(serie -> new SerieDTO(serie.getId(), serie.getTitulo(), serie.getTotalTemporadas(), serie.getAvaliacao(),
                        serie.getGenero(), serie.getImagemPoster(), serie.getAtores(),serie.getSinopse())).toList();
    }

    public List<SerieDTO> obterTodasAsSeries() {
       return converterParaSerieDTO(repositorio.findAll());
    }

    public List<SerieDTO> obterTop5Series() {
        return  converterParaSerieDTO(repositorio.findTop5ByOrderByAvaliacaoDesc());
    }

    public SerieDTO obterSeriePorId(Long id) {
        Optional<Serie> serie = repositorio.findById(id);

        if(serie.isPresent()) {
            Serie s = serie.get();
            return new SerieDTO(s.getId(), s.getTitulo(), s.getTotalTemporadas(), s.getAvaliacao(), s.getGenero(),
                    s.getImagemPoster(), s.getAtores(), s.getSinopse());
        }
        return null;
    }

    public List<SerieDTO> obterLancamentos() {
        return converterParaSerieDTO(repositorio.buscaPorMaisAtual());
    }

    public List<EpisodioDTO> obterTemporadas(Long id) {
        Optional<Serie> serie = repositorio.findById(id);

        if(serie.isPresent()) {
            Serie s = serie.get();
            return s.getEpisodios().stream()
                    .map(episodio -> new EpisodioDTO(episodio.getTemporada(), episodio.getTitulo(), episodio.getNumeroEpisodio())).toList();
        }
        return null;
    }

    public List<EpisodioDTO> obterTemporadasPorNumero(Long id, Long idTemp) {
        return repositorio.obterEpisodiosPorTemporada(id, idTemp).stream()
                .map(e -> new EpisodioDTO(e.getTemporada(), e.getTitulo(), e.getNumeroEpisodio())).toList();
    }

    public List<SerieDTO> obterSeriePorGenero(String genero) {
        Categoria categoria = Categoria.fromPortugues(genero);
        return converterParaSerieDTO(repositorio.findByGenero(categoria));
    }

    public List<EpisodioDTO> obterTop5Episodios(Long id) {
        return repositorio.obterTop5Episodios(id).stream()
                .map(e -> new EpisodioDTO(e.getNumeroEpisodio(), e.getTitulo(), e.getTemporada())).toList();
    }
}
