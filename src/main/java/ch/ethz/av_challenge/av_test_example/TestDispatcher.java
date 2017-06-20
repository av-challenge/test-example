package ch.ethz.av_challenge.av_test_example;

import ch.ethz.matsim.av.config.AVDispatcherConfig;
import ch.ethz.matsim.av.data.AVVehicle;
import ch.ethz.matsim.av.dispatcher.AVDispatcher;
import ch.ethz.matsim.av.passenger.AVRequest;

public class TestDispatcher implements AVDispatcher{
    public void onRequestSubmitted(AVRequest avRequest) {
        //System.err.println("onRequestSubmitted");
    }

    public void onNextTaskStarted(AVVehicle avVehicle) {
        //System.err.println("onNextTaskStarted");
    }

    public void onNextTimestep(double v) {
        //System.err.println("onNextTimestep");
    }

    public void addVehicle(AVVehicle avVehicle) {
        //System.err.println("addVehicle");
    }

    static public class TestDispatcherFactory implements AVDispatcherFactory {
        public AVDispatcher createDispatcher(AVDispatcherConfig avDispatcherConfig) {
            return new TestDispatcher();
        }
    }
}
