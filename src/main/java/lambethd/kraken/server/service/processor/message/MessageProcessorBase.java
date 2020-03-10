package lambethd.kraken.server.service.processor.message;

public abstract class MessageProcessorBase implements  IMessageProcessor {

    public abstract void ProcessMessage(String message);

    @Override
    public boolean CanProcess(MessageType messageType) {
        return GetApplicableMessageType() == messageType;
    }

    abstract MessageType GetApplicableMessageType();
}
