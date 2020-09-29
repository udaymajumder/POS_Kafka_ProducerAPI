# POS_Kafka_ProducerAPI
Realtime POS Invoice generation using Kafka Producer API (with Key[InvoiceNo:Integer],Value[JSON:String])

For invoice details please refer to org.example.POS_Realtime.POJO.EntityMapper.Invoice

The application works like a POS emulator which generating invoice continuously on the fly. Speed of invoice generation can be controlled through the application. Generated invoice is transformed into JSON and pushed to kafka topic.
