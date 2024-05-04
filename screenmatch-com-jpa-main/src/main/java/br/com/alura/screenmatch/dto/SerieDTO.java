package br.com.alura.screenmatch.dto;

import br.com.alura.screenmatch.model.Categoria;

//Aqui eu decido que campos vou distribuir para o frontend.
public record SerieDTO(
        Long id,
        String titulo,
        Integer totalTemporadas,
        Double avaliacao,
        Categoria genero,
        String imagemPoster,
        String atores,
        String sinopse
) {
}
