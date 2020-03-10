package lambethd.kraken.server.service.processor.message;

public interface IMessageProcessor {
    void ProcessMessage(String message);
    boolean CanProcess(MessageType messageType);
}
