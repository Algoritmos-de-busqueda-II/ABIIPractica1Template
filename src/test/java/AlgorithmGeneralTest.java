import es.urjc.grafo.ABII.Algorithms.Algorithm;
import es.urjc.grafo.ABII.Model.Evaluator;
import es.urjc.grafo.ABII.Model.Instance;
import es.urjc.grafo.ABII.Model.Solution;
import org.junit.jupiter.api.Assertions;

import java.io.File;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class AlgorithmGeneralTest {

    private static Map<String, Double> expectedQuality = new HashMap<>();

    private static void fillMap(){
        expectedQuality.put("a_1_n10_m2.txt", 0.0);
        expectedQuality.put("a_16_n10_m6.txt", 0.0);
        expectedQuality.put("a_19_n10_m6.txt", 0.0);
        expectedQuality.put("a_23_n10_m8.txt", 0.0);
        expectedQuality.put("a_27_n15_m3.txt", 0.0);
        expectedQuality.put("a_44_n15_m9.txt", 0.0);
        expectedQuality.put("a_49_n15_m12.txt", 0.0);
        expectedQuality.put("a_54_n30_m6.txt", 0.0);
        expectedQuality.put("a_71_n30_m24.txt", 0.0);
        expectedQuality.put("a_75_n30_m24.txt", 0.0);
    }

    public static void generalTest(String instancePath, Algorithm algorithm, long maxTimePerInstance) {
        try {
            fillMap();
            File folder = new File(instancePath);
            long numberOfInstances = 0;
            Instant instant = Instant.now();
            for (File fileEntry : folder.listFiles()) {
                numberOfInstances++;
                Instance instance = new Instance(instancePath + File.separator + fileEntry.getName());
                Solution solution = algorithm.run(instance);
                Assertions.assertTrue(Evaluator.isFeasible(solution, instance), "La solución no es factible");
                double score = Evaluator.evaluate(solution, instance);
                System.out.println("Para la instancia " + fileEntry.getName() +
                        ", la calidad de la solución generada por el algoritmo "
                        + algorithm.toString() + " es " + score);
                Assertions.assertTrue(
                        score >= expectedQuality.getOrDefault(fileEntry.getName(), 0.0),
                        "La calidad de la solución no es suficiente");
            }
            Duration elapsedTime = Duration.between(instant, Instant.now());
            Assertions.assertTrue(elapsedTime.getSeconds() < (maxTimePerInstance * numberOfInstances),
                    "El algoritmo ha tardado más de un minuto de media");
        }
        catch (UnsupportedOperationException e) {
            Assertions.fail(algorithm.toString() + " no está implementado");
        }
        catch (Exception e) {
            Assertions.fail("Error en la ejecución del algoritmo");
        }
    }
}
