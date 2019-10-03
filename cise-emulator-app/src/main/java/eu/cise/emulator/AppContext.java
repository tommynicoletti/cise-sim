package eu.cise.emulator;

import eu.cise.dispatcher.Dispatcher;
import eu.cise.emulator.api.CiseEmulatorAPI;
import eu.cise.emulator.io.MessageStorage;
import eu.cise.signature.SignatureService;

public interface AppContext {

    MessageProcessor makeMessageProcessor();

    DefaultEmulatorEngine makeEmulatorEngine();

    Dispatcher makeDispatcher();

    SignatureService makeSignatureService();

    CiseEmulatorAPI makeEmulatorApi(MessageProcessor messageProcessor, MessageStorage messageStorage);

    MessageStorage makeMessageStorage();
}
