package br.com.alura.screenmatch.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

@Entity
@Table(name = "series")

public class Serie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String titulo;
    private Integer totalTemporadas;
    private Double avaliacao;
    @Enumerated(EnumType.STRING)
    private Categoria genero;
    private String atores;
    private String imagemPoster;
    private String sinopse;

    //Enxergando a série, de uma série para muitos episódios, após isso mapear também em episódio para ficar bidirecional.
    //E passar como parâmetro por onde está sendo mapeado na OUTRA classe.
    @OneToMany(mappedBy = "serie", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Episodio> episodios = new ArrayList<>();

    //Construtor padrão exigido pela JPA para consultar os dados e representar como um objeto do tipo série.
    public Serie() {}

    public Serie(DadosSerie dadosSerie) {
        this.titulo          = dadosSerie.titulo();
        this.totalTemporadas = dadosSerie.totalTemporadas();
                               //Optional, pois as vezes as séries vem sem avaliação, double value of(teste feito
        //                                              anteriormente se era numero ou nao, )
        this.avaliacao       = OptionalDouble.of(Double.parseDouble(dadosSerie.avaliacao())).orElse(0);
        this.genero          = Categoria.fromString(dadosSerie.genero().split(",")[0].trim());
        this.atores          = dadosSerie.atores();
        this.imagemPoster    = dadosSerie.imagemPoster();
        this.sinopse         = dadosSerie.sinopse();
    }

    public List<Episodio> getEpisodios() {
        return episodios;
    }

    public void setEpisodios(List<Episodio> episodios) {
        episodios.forEach(e -> e.setSerie(this));
        this.episodios = episodios;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getTotalTemporadas() {
        return totalTemporadas;
    }

    public void setTotalTemporadas(Integer totalTemporadas) {
        this.totalTemporadas = totalTemporadas;
    }

    public Double getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(Double avaliacao) {
        this.avaliacao = avaliacao;
    }

    public Categoria getGenero() {
        return genero;
    }

    public void setGenero(Categoria genero) {
        this.genero = genero;
    }

    public String getAtores() {
        return atores;
    }

    public void setAtores(String atores) {
        this.atores = atores;
    }

    public String getImagemPoster() {
        return imagemPoster;
    }

    public void setImagemPoster(String imagemPoster) {
        this.imagemPoster = imagemPoster;
    }

    public String getSinopse() {
        return sinopse;
    }

    public void setSinopse(String sinopse) {
        this.sinopse = sinopse;
    }

    @Override
    public String toString() {
        return System.out.printf("""

                        Título: %s
                        Total de Temporadas: %d
                        Avaliação: %.2f
                        Gênero: %s
                        Atores: %s
                        Poster: %s
                        Sinopse: %s""",
                          titulo,
                          totalTemporadas,
                          avaliacao,
                          genero,
                          atores,
                          imagemPoster,
                          sinopse).toString();
    }
}
