package dk.artogis.hepwat.onlineservice.clientmessage;

public class SubscribtionMessage extends Message {
    Subscription [] subscriptions;

    public Subscription[] getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(Subscription[] subscriptions) {
        this.subscriptions = subscriptions;
    }

    public String getString()
    {
        String subscriptionMessagesText =  "";
        for (Subscription subscription : subscriptions) {
            subscriptionMessagesText += subscription.getString() + " ";
        }

        return  subscriptionMessagesText;
    }
}
