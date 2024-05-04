package br.com.alura.screenmatch.model;

public enum Categoria {

    // Em um ENUM, primeiro vem as enumerações, depois as declarações de variáveis, e depois o método construtor.

    ACAO("Action", "Ação"),
    DRAMA("Drama", "Drama"),
    FANTASIA("Fantasy", "Fantasia"),
    HORROR("Horror", "Terror"),
    SUSPENSE("Suspense", "Suspense"),
    ROMANCE("Romance", "Romance"),
    CRIME("Crime", "Crime"),
    AVENTURA("Adventure", "Aventura"),
    COMEDIA("Comedy", "Comédia");

    private String categoriaOmdb;
    private String categoriaPortugues;

    Categoria(String categoriaOmdb, String categoriaPortugues){
        this.categoriaOmdb = categoriaOmdb;
        this.categoriaPortugues = categoriaPortugues;
    }

    public static Categoria fromString(String text) {
        for (Categoria categoria : Categoria.values()) {
            if (categoria.categoriaOmdb.equalsIgnoreCase(text)) {
                return categoria;
            }
        }
        throw new IllegalArgumentException("Nenhuma categoria encontrada para a string fornecida: " + text);
    }

    public static Categoria fromPortugues(String text) {
        for (Categoria categoria : Categoria.values()) {
            if(categoria.categoriaPortugues.equalsIgnoreCase(text)){
                return categoria;
            }
        }
        throw new IllegalArgumentException("Nenhuma categoria encontrada para a string fornecida: " + text);
    }
}


