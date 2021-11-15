package me.schawe.autosnake;

import org.deeplearning4j.nn.graph.ComputationGraph;
import org.deeplearning4j.nn.modelimport.keras.KerasModelImport;
import org.nd4j.common.io.ClassPathResource;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;


// https://deeplearning4j.konduit.ai/deeplearning4j/how-to-guides/keras-import
public class Autopilot {
    private ComputationGraph model;

    public Autopilot(String pathToModel) {
        try {
            String savedModel = new ClassPathResource(pathToModel).getFile().getPath();
            model = KerasModelImport.importKerasModelAndWeights(savedModel, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // infer the next move from the given state
    public int nextMove(boolean[] state) {

        // since the model expects a batch of states, we have to `reshape`
        // our input, i.e., the `1` specifies that our batch size is 1
        INDArray input = Nd4j.create(state).reshape(1, state.length);

        // the output is also batched, since we have only one, we take the first with [0]
        INDArray output = model.output(input)[0];

        int action = output.ravel().argMax().getInt(0);

        return action;
    }
}