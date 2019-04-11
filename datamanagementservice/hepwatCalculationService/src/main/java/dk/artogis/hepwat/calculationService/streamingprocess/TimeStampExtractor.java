package dk.artogis.hepwat.calculationService.streamingprocess;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.streams.processor.TimestampExtractor;


public class TimeStampExtractor implements TimestampExtractor {

        @Override
        public long extract(final ConsumerRecord<Object, Object> record, final long previousTimestamp) {
            // `Foo` is your own custom class, which we assume has a method that returns
            // the embedded timestamp (milliseconds since midnight, January 1, 1970 UTC).
            long timestamp = -1;
            final JsonNode myPojo = (JsonNode) record.value();
            if (myPojo != null) {
                try {
                    timestamp = myPojo.get("timestamp").asLong();
                }
                catch (Exception ex)
                {
                   timestamp = System.currentTimeMillis();
                }
                //System.out.println(String.format("time %d",timestamp));
            }
            if (timestamp < 0) {
                // Invalid timestamp!  Attempt to estimate a new timestamp,
                // otherwise fall back to wall-clock time (processing-time).
                if (previousTimestamp >= 0) {
                    return previousTimestamp;
                } else {
                    return System.currentTimeMillis();
                }
            }
            return timestamp;
        }

    }


