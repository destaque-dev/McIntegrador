package main;

import bean.Configuracao;
import exception.IntegradorException;
import integracao.IntegradorBase;
import integracao.IntegradorExemplo;
import util.Funcoes;
import util.Log;

public class IniciaMcIntegrador {

    private static boolean stop = false;

    public static void start(String[] args) {

        try {
            Log.info("Iniciando McIntegrador...");

            Configuracao configuracao = Configuracao.getInstance();

            IntegradorBase integrador = montaIntegrador(configuracao);

            while (!isStop()) {

                try {
                    integrador.verificaRegistrosNovos();
                    integrador.verificaRegistrosAtualizados();
                    integrador.verificaRegistrosRemovidos();
                    integrador.verificaRegistrosEspeciais();

                    // Espera um minuto
                    Thread.sleep(60000);
                } catch (Exception e) {
                    Funcoes.trataErro(e, integrador);
                    try {
                        Thread.sleep(60000);
                    } catch (InterruptedException e1) {

                    }

                }

            }
        } catch (Exception e) {
            Funcoes.trataErro(e, null);
            try {
                Thread.sleep(60000);
            } catch (InterruptedException e1) {

            }
            start(args);
        }

    }

    private static IntegradorBase montaIntegrador(Configuracao configuracao) throws Exception {

        IntegradorBase integrador = montaIntegrador();

        if (integrador == null) {
            throw new IntegradorException("Integrador correto não encontrado, reveja o .properties");
        }

        integrador.inicializa(configuracao);

        return integrador;
    }

    public static void stop(String[] args) {

        Log.info("Parando McIntegrador");
        stop = true;
    }

    public static void main(String[] args) {

        if (args == null || args.length == 0 || "start".equals(args[0])) {
            start(args);
        } else if ("stop".equals(args[0])) {
            stop(args);
        }
    }

    public static boolean isStop() {

        return stop;
    }

    public static IntegradorBase montaIntegrador() throws Exception {

		return new IntegradorExemplo();
    }

}
