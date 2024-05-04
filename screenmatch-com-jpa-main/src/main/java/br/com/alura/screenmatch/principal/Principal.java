package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.model.*;
import br.com.alura.screenmatch.repository.SerieRepository;
import br.com.alura.screenmatch.service.ConsumoApi;
import br.com.alura.screenmatch.service.ConverteDados;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    private Scanner leitura = new Scanner(System.in);
    private ConsumoApi consumo = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=b3935dc4";
    private SerieRepository repositorio;
    private List<Serie> series;
    Optional<Serie> serieBuscada;

    public Principal(SerieRepository repositorio) {
        this.repositorio = repositorio;
    }

    public void exibeMenu() {
        var opcao = -1;

        while(opcao != 0) {
            var menu = """ 
                
                1 - Buscar séries
                2 - Buscar episódios
                3 - Exibir lista de séries adicionadas
                4 - Buscar série por nome
                5 - Buscar por ator
                6 - Top 5 séries
                7 - Buscar por categoria
                8 - Buscar por número de temporadas
                9 - Buscar episódio por trecho
                10 - Top 5 episódios por série 
                11 - Buscar episódio a partir de uma data
                
                0 - Sair                                 
                """;

            System.out.println(menu);
            opcao = leitura.nextInt();
            leitura.nextLine();

            switch (opcao) {
                case 1:
                    buscarSerieWeb();
                    break;
                case 2:
                    buscarEpisodioPorSerie();
                    break;
                case 3:
                    exibeLista();
                    break;
                case 4:
                    buscarSeriePorNome();
                    break;
                case 5:
                    buscarPorAtor();
                    break;
                case 6:
                    buscarTop5();
                    break;
                case 7:
                    buscarPorCategoria();
                case 8:
                    buscarPorNumeroDeTemporadas();
                    break;
                case 9:
                    buscarEpisodioPorTrecho();
                    break;
                case 10:
                    top5EpisodioPorSerie();
                    break;
                case 11:
                    buscarEpisodioAPartirDeUmaData();
                    break;
                case 0:
                    System.out.println("\nSaindo...");
                    break;
                default:
                    System.out.println("Opção inválida");
            }
        }
    }

    private void buscarSerieWeb() {
        DadosSerie dados = getDadosSerie();
        Serie serie = new Serie(dados);
        //dadosSerie.add(dados);
        //Utilizando o repositorio manuseado pelo spring/JPA para salvar dados no banco de dados.
        repositorio.save(serie);
        System.out.println("\n" + dados + "\n");
    }

    private DadosSerie getDadosSerie() {
        System.out.print("Digite o nome da série para busca: ");
        var nomeSerie = leitura.nextLine();
        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        System.out.println(dados);
        return dados;
    }

    private void buscarEpisodioPorSerie(){
        exibeLista();
        System.out.print("\nEscolha uma série pelo nome: ");
        var serieEscolhida = leitura.nextLine();

        Optional<Serie> serie = repositorio.findByTituloContainingIgnoreCase(serieEscolhida);

        if (serie.isPresent()) {
            // Transformando o Optional serie em um objeto do tipo Serie;
            var serieEncontrada = serie.get();
            List<DadosTemporada> temporadas = new ArrayList<>();

            for (int i = 1; i <= serieEncontrada.getTotalTemporadas(); i++) {
                var json = consumo.obterDados(ENDERECO + serieEncontrada.getTitulo().replace(" ", "+") + "&season=" + i + API_KEY);
                DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
                temporadas.add(dadosTemporada);
            }
            temporadas.forEach(System.out::println);

            List<Episodio> episodios = temporadas.stream() // Flatmap para pegar a lista de episodios dentro da lista de temporadas, e para cada elemento usar o map para criar um novo objeto do tipo Episódio.
                    .flatMap(d -> d.episodios()
                            .stream()
                            .map(e -> new Episodio(d.numero(), e))).collect(Collectors.toList());

            serieEncontrada.setEpisodios(episodios);
            repositorio.save(serieEncontrada);
        } else {
            System.out.println("Série não encontrada!");
        }
    }

    private void exibeLista() {

        //Buscando os dados do repositório com o método findAll do JPA, que nos retorna uma list do tipo de dado generico(definido).
        series = repositorio.findAll();
        series.stream()
                .sorted(Comparator.comparing(Serie::getTitulo))
                .forEach(System.out::println);

    }

    private void buscarSeriePorNome() {
        System.out.print("\nEscolha uma série pelo nome: ");
        var serieEscolhida = leitura.nextLine();

        serieBuscada = repositorio.findByTituloContainingIgnoreCase(serieEscolhida);

        if (serieBuscada.isPresent()) {
            System.out.println(serieBuscada.get());
        } else {
            System.out.println("Série não encontrada!");
        }
    }

    private void buscarPorAtor() {
        System.out.print("\nInforme o nome do ator ou atriz: ");
        var atorBuscado = leitura.nextLine();
        System.out.print("\nInforme a avaliação mínima para as séries: ");
        var avaliacao = leitura.nextDouble();

        List<Serie> series = repositorio.findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(atorBuscado, avaliacao);

        series.forEach(System.out::println);
    }

    private void buscarTop5() {
        List<Serie> series = repositorio.findTop5ByOrderByAvaliacaoDesc();
        series.forEach(System.out::println);
    }

    private void buscarPorCategoria() {
        System.out.print("\nInforme a categoria/gênero de série que deseja ver: ");
        var categoriaEscolhida = leitura.nextLine();
        Categoria categoria = Categoria.fromPortugues(categoriaEscolhida);
        List<Serie> series = repositorio.findByGenero(categoria);

        series.forEach(System.out::println);
    }

    private void buscarPorNumeroDeTemporadas() {
        System.out.print("\nInforme a quantidade máxima de temporadas para a séries: ");
        var quantidadeMaxima = leitura.nextInt();
        leitura.nextLine();
        System.out.print("\nInforme a avaliação mínima para as séries: ");
        var avaliacao = leitura.nextDouble();

        List<Serie> series = repositorio.buscarPorTotalTemporadas(quantidadeMaxima, avaliacao);

        series.forEach(System.out::println);
    }

    private void buscarEpisodioPorTrecho() {
        System.out.print("\nInforme o trecho do nome do episódio para a busca: ");
        var trechoEpisodio = leitura.nextLine();

        List<Episodio> episodiosEncontrados = repositorio.buscarEpisodioPorTrecho(trechoEpisodio);

        episodiosEncontrados.forEach(e ->
                System.out.printf("\nSérie: %s \nTemporada: %s \nEpisódio: %s \nTítulo do episódio: %s\n",
                        e.getSerie().getTitulo(), e.getTemporada(),
                        e.getNumeroEpisodio(), e.getTitulo()));
    }

    private void top5EpisodioPorSerie() {
        buscarSeriePorNome();

        if (serieBuscada.isPresent()) {
            Serie serie = serieBuscada.get();

            List<Episodio> topEpisodios = repositorio.top5EpisodiosPorSerie(serie);

            topEpisodios.forEach(e ->
                    System.out.printf("\nSérie: %s \nTemporada: %s \nEpisódio: %s \nTítulo do episódio: %s \nAvaliação do Episódio: %s \n",
                            e.getSerie().getTitulo(), e.getTemporada(),
                            e.getNumeroEpisodio(), e.getTitulo(), e.getAvaliacao()));
        }
    }

    private void buscarEpisodioAPartirDeUmaData() {
        buscarSeriePorNome();

        if (serieBuscada.isPresent()) {
            Serie serie = serieBuscada.get();
            System.out.print("\nInforme o ano de lançamento que deseja ver: ");
            var dataLancamento = leitura.nextInt();
            leitura.nextLine();

            List<Episodio> episodioPorData = repositorio.buscarEpisodioPorData(serie, dataLancamento);

            episodioPorData.forEach(e ->
                    System.out.printf("\nSérie: %s \nTemporada: %s \nEpisódio: %s \nTítulo do episódio: %s\n",
                            e.getSerie().getTitulo(), e.getTemporada(),
                            e.getNumeroEpisodio(), e.getTitulo()));
        }
    }

}