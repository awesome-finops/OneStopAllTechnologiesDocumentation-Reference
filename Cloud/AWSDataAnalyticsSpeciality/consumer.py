import os, logging
import sys
import boto3
from dotenv import load_dotenv
from context import src_dir, data_dir
from AWSDataAnalyticsSpeciality.s3.aws_s3_services import S3
from AWSDataAnalyticsSpeciality.kinesis.aws_kinesis_services import KinesisStream
import argparse

logger = logging.getLogger(__name__)
parser = argparse.ArgumentParser()
print(os.path.join(src_dir, '.env'))
print(data_dir)
load_dotenv(os.path.join(src_dir, '.env'))
# print(os.getenv("aws_access_key_id"))
# print(os.getenv("aws_secret_access_key"))
client = boto3.client('kinesis', aws_access_key_id=os.getenv("aws_access_key_id"),
                      aws_secret_access_key=os.getenv("aws_secret_access_key"))
s3_client = boto3.resource('s3',aws_access_key_id=os.getenv("aws_access_key_id"),
                      aws_secret_access_key=os.getenv("aws_secret_access_key"))

parser.add_argument("--get_records", help="get records from kinesis stream.", default=True, required=False)
parser.add_argument("--create_bucket", help="create bucket in S3.", default=True, required=False)
args = parser.parse_args()

if args.create_bucket:
    S3_obj = S3()
    S3_obj.create_bucket(s3_client)

sys.exit()
if args.get_records:
    kinesis_obj = KinesisStream(client)
    kinesis_obj.describe()
    # get records from the stream
    records = kinesis_obj.fetch_records(100)
    for record in records:
        print(record)






