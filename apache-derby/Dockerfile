FROM amazoncorretto:21-alpine

# Set environment variables for Derby
ENV DERBY_NAME db-derby-10.17.1.0-bin
ENV DERBY_HOME /opt/$DERBY_NAME
ENV PATH $DERBY_HOME/bin:$PATH

# Download and extract Apache Derby
RUN wget https://dlcdn.apache.org//db/derby/db-derby-10.17.1.0/$DERBY_NAME.tar.gz && \
    tar -xzf $DERBY_NAME.tar.gz -C /opt/ && \
    rm $DERBY_NAME.tar.gz && \
    rm -rf /var/lib/apt/lists/*

# Create a database directory
RUN mkdir -p /usr/local/derby_data

# Set the working directory
WORKDIR /usr/local/derby_data

# Expose Derby's default port
EXPOSE 1527

# Start Derby Network Server
CMD java -jar $DERBY_HOME/lib/derbyrun.jar server start
#CMD ["java", "-jar", "$DERBY_HOME/lib/derbyrun.jar", "server start"]
