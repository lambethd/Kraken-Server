package lambethd.kraken.server.configuration;


import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ResourceInUseException;
import lambethd.kraken.data.repository.dto.ConfigDto;
import lambethd.kraken.data.repository.dto.JobDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RepositorySetup {
    @Autowired
    private AmazonDynamoDB amazonDynamoDB;

    private DynamoDBMapper dynamoDBMapper;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public void setup() {
        createAllTables();
        setupJobDetail();
    }

    private void createAllTables(){
        dynamoDBMapper = new DynamoDBMapper(amazonDynamoDB);
        createTable(JobDto.class);
        createTable(ConfigDto.class);
    }

    private void createTable(Class<?> clazz){
        try {
            CreateTableRequest tableCreationRequest = dynamoDBMapper.generateCreateTableRequest(clazz);
            tableCreationRequest.setProvisionedThroughput(new ProvisionedThroughput(1L, 1L));
            amazonDynamoDB.createTable(tableCreationRequest);
        }catch(ResourceInUseException e){
            logger.info(clazz.getSimpleName() + " table already created");
        }
    }

    private void setupJobDetail() {
        logger.info("Setting up JobDetail");
//        Resource resource = new ClassPathResource("JobDetail.json");
//        try {
//            String jobDetail = String.join("\n", Files.readAllLines(resource.getFile().toPath()));
//            jobDetailRepository.deleteAll();
//            JobDetail[] jobDetails = new ObjectMapper().readValue(jobDetail, JobDetail[].class);
//            jobDetailRepository.saveAll(Arrays.asList(jobDetails));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        logger.info("Finished setting up JobDetail");
    }
}
