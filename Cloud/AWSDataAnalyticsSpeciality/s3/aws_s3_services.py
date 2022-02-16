from config.config import config
import logging
import boto3
from botocore.exceptions import ClientError





class S3:
    def __init__(self):
        self.config = config
        self.name = self.config["S3"]["BucketName"]

    def create_bucket(self, client):
        """Create an S3 bucket in a specified region
        If a region is not specified, the bucket is created in the S3 default
        region (us-east-1).
        """
        # Create bucket
        try:
            location = {'LocationConstraint': client.meta.client.meta.region_name}
            client.create_bucket(Bucket=self.name)
        except ClientError as e:
            logging.error(e)
            return False
        return True

    def upload(self):
        # upload
        pass

    def download(self):
        #download
        pass
