import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class T1 extends Thread{
    private Monitor monitor;
    private int cantidadCeros;
    private String mensajePlano;
    private MessageDigest algoritmo;
    private String cadenaCeros;
    private String repBinaria;
    private String mensajeEncontrado;
    private String hashEncontrado;

    public T1(int cantidadCerosP, String mensajePlanoP, MessageDigest algoritmoP, String repBinariaP, Monitor monitorP) {
        cantidadCeros = cantidadCerosP;
        mensajePlano = mensajePlanoP;
        algoritmo = algoritmoP;
        cadenaCeros = "0".repeat(cantidadCeros);
        repBinaria = repBinariaP;
        monitor = monitorP;
    }

    @Override
    public void run() {
        System.out.println("Ejecutando T1");
        System.out.println("Cantidad de 0's al inicio: " + cantidadCeros);
        System.out.println("Mensaje plano: " + mensajePlano);
        System.out.println("Representación binaria del hash: " + repBinaria);
        System.out.println("Algoritmo de Hash: " + algoritmo.getAlgorithm());

        boolean encontrado = false;
        String abecedario = "abcdefghijklmnopqrstuvwxyz";
        long inicio = System.currentTimeMillis()/1000;

        while (!encontrado && !monitor.getEncontrado()) {
            for (int tamano = 1; tamano <= 7 && !encontrado; tamano++) {
                encontrado = generarCombinaciones(mensajePlano, abecedario, tamano);
            }
        }
        
        if (encontrado && !monitor.getEncontrado()) {
            monitor.setEncontrado(true);
            long fin = System.currentTimeMillis()/1000;
            long tiempoTranscurrido = fin - inicio;
            System.out.println("\n++++++++++++++++++++++++++++++++++++++++++++");
            System.out.println("Mensaje encontrado por T1: " + mensajeEncontrado);
            System.out.println("V usado (Concatenado al final): " + mensajeEncontrado.substring(mensajePlano.length()));
            System.out.println("Tiempo transcurrido: " + tiempoTranscurrido + " segundos");
            System.out.println("Hash del mensaje encontrado: " + hashEncontrado);
            System.out.println("++++++++++++++++++++++++++++++++++++++++++++");
        }
        System.out.println("T1 termino");
    }

    private boolean generarCombinaciones(String mensajeInicial, String abecedario, int length) {
        if (length == 0) {
            String mensajeConcatenado = mensajeInicial;
            String hash = calcularHash(algoritmo, mensajeConcatenado);

            if (monitor.getEncontrado()) {
                return true;
            }

            if (hash.startsWith(cadenaCeros)) {
                mensajeEncontrado = mensajeConcatenado;
                hashEncontrado = hash;
                return true;
            }
            return false;
        }

        for (int i = 0; i < abecedario.length(); i++) {
            if (generarCombinaciones(mensajeInicial + abecedario.charAt(i), abecedario, length - 1)) {
                return true;
            }
        }
        return false;
    }

    private String calcularHash(MessageDigest algoritmo, String mensaje) {
        byte[] hashBytes = algoritmo.digest(mensaje.getBytes(StandardCharsets.UTF_8));

        StringBuilder repBinaria = new StringBuilder();
        for (byte b : hashBytes) {
            for (int i = 7; i >= 0; i--) {
                int bit = (b >> i) & 0x01;
                repBinaria.append(bit);
            }
        }

        return repBinaria.toString();
    }
}
