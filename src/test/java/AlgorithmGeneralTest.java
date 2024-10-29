import es.urjc.grafo.ABII.Algorithms.Algorithm;
import es.urjc.grafo.ABII.Model.Evaluator;
import es.urjc.grafo.ABII.Model.Instance;
import es.urjc.grafo.ABII.Model.Solution;
import org.junit.jupiter.api.Assertions;

import java.io.File;
import java.time.Duration;
import java.time.Instant;

public class AlgorithmGeneralTest {

    public static void generalTest(String instancePath, Algorithm algorithm, long maxTimePerInstance) {
        try {
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
                        " el score del algoritmo " + algorithm.toString() + " es " + score);
                //Assertions.assertTrue(score >= expectedQuality, "La calidad de la solución no es suficiente");
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
