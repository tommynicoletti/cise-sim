package eu.cise.sim.api.history;

import eu.cise.servicemodel.v1.message.Message;
import eu.cise.sim.utils.Pair;

import java.util.List;

public interface HistoryAPI {

    List<Pair<Message, Boolean>> getLatestMessages();

}
