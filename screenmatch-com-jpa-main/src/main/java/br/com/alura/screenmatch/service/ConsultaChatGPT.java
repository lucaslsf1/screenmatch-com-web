//package br.com.alura.screenmatch.service;
//
//import com.theokanning.openai.completion.CompletionRequest;
//import com.theokanning.openai.service.OpenAiService;
//
//public class ConsultaChatGPT {
//    public static String obterTraducao(String texto) {
//
//        OpenAiService service = new OpenAiService("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");
//
//        CompletionRequest requisicao = CompletionRequest.builder().model("text-davinci-003")
//                .prompt("Traduza para portugês-br o texto: " + texto)
//                .maxTokens(1000)
//                .temperature(0.7)
//                .build();
//
//        var resposta = service.createCompletion(requisicao);
//        return resposta.getChoices().get(0).getText();
//    }
//}
