package me.schawe.autosnake;

import org.deeplearning4j.nn.graph.ComputationGraph;
import org.deeplearning4j.nn.modelimport.keras.KerasModelImport;
import org.deeplearning4j.nn.modelimport.keras.exceptions.InvalidKerasConfigurationException;
import org.deeplearning4j.nn.modelimport.keras.exceptions.UnsupportedKerasConfigurationException;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.common.io.ClassPathResource;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import java.io.IOException;
import java.util.List;

// https://deeplearning4j.konduit.ai/deeplearning4j/how-to-guides/keras-import
public class Autopilot {
    ComputationGraph modelFunctional;
    String pathToModel;

    public Autopilot(String pathToModel) {
        this.pathToModel = pathToModel;

        try {
            String savedModel = new ClassPathResource(pathToModel).getFile().getPath();
            modelFunctional = KerasModelImport.importKerasModelAndWeights(savedModel, false);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnsupportedKerasConfigurationException e) {
            e.printStackTrace();
        } catch (InvalidKerasConfigurationException e) {
            e.printStackTrace();
        }
    }

    // infer the next move from the given state
    public int nextMove(List<Integer> state) {

        // since the model expects a batch of states, we have to `reshape`
        // our input, i.e., the `1` specifies that our batch size is 1
        INDArray input = Nd4j.create(state).reshape(1, state.size());

        // the output is also batched, since we have only one, we take the first with [0]
        INDArray output = modelFunctional.output(input)[0];

        int action = output.ravel().argMax().getInt(0);

        return action;
    }
}