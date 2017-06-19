package ch.ethz.av_challenge.av_test_example;

import ch.ethz.matsim.av.framework.AVConfigGroup;
import ch.ethz.matsim.av.framework.AVModule;
import ch.ethz.matsim.av.framework.AVQSimProvider;
import ch.ethz.matsim.av.framework.AVUtils;
import ch.ethz.matsim.sioux_falls.SiouxFallsUtils;
import org.matsim.api.core.v01.Scenario;
import org.matsim.contrib.dvrp.run.DvrpConfigGroup;
import org.matsim.contrib.dvrp.trafficmonitoring.VrpTravelTimeModules;
import org.matsim.contrib.dynagent.run.DynQSimModule;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.config.groups.PlanCalcScoreConfigGroup;
import org.matsim.core.config.groups.StrategyConfigGroup;
import org.matsim.core.controler.AbstractModule;
import org.matsim.core.controler.Controler;
import org.matsim.core.scenario.ScenarioUtils;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class RunExample {
    static public void main(String[] args) {
        AVConfigGroup avConfigGroup = new AVConfigGroup();
        avConfigGroup.setConfigURL(RunExample.class.getResource("av.xml"));

        Config config = ConfigUtils.loadConfig(SiouxFallsUtils.getConfigURL(), avConfigGroup, new DvrpConfigGroup());
        Scenario scenario = ScenarioUtils.loadScenario(config);

        List<String> modes = new LinkedList<>(Arrays.asList(config.subtourModeChoice().getModes()));
        modes.add(AVModule.AV_MODE);
        config.subtourModeChoice().setModes(modes.toArray(new String[modes.size()]));

        StrategyConfigGroup.StrategySettings settings = new StrategyConfigGroup.StrategySettings();
        settings.setStrategyName("AVOperatorChoice");
        settings.setWeight(0.2);
        config.strategy().addStrategySettings(settings);

        PlanCalcScoreConfigGroup.ModeParams params = config.planCalcScore().getOrCreateModeParams(AVModule.AV_MODE);
        params.setConstant(0.0);
        params.setMonetaryDistanceRate(0.0);
        params.setMarginalUtilityOfTraveling(8.86);

        Controler controler = new Controler(scenario);
        controler.addOverridingModule(VrpTravelTimeModules.createTravelTimeEstimatorModule());
        controler.addOverridingModule(new DynQSimModule<>(AVQSimProvider.class));
        controler.addOverridingModule(new AVModule());

        controler.addOverridingModule(new AbstractModule() {
            @Override
            public void install() {
                AVUtils.registerDispatcherFactory(binder(), "TestDispatcher", TestDispatcher.TestDispatcherFactory.class);
            }
        });

        controler.run();
    }
}
