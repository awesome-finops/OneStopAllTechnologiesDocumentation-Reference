import os, logging
import pandas as pd
import sys
import boto3
from dotenv import load_dotenv
from context import src_dir, data_dir
from s3.aws_s3_services import S3
from kinesis.aws_kinesis_services import KinesisStream
import argparse
logger = logging.getLogger(__name__)
parser = argparse.ArgumentParser()
load_dotenv(os.path.join(src_dir, '.env'))
print(os.path.join(src_dir, '.env'))
print(os.getenv("aws_access_key_id"))
print(os.getenv("aws_secret_access_key"))
client = boto3.client('kinesis', aws_access_key_id=os.getenv("aws_access_key_id"),
                      aws_secret_access_key=os.getenv("aws_secret_access_key"))
parser.add_argument("--create_stream", help="create kinesis stream.", default=False, required=False)
parser.add_argument("--put_records", help="put records in kinetic stream.", default=True, required=False)
args = parser.parse_args()
logger.info("Reading the events files from {stream} environment.".format(stream=args.create_stream))

# produce message and stream through kinesis data stream
if args.create_stream:
    kinesis_obj = KinesisStream(client)
    # create stream
    kinesis_obj.create()
if args.put_records:
    kinesis_obj = KinesisStream(client)
    # create stream
    details = kinesis_obj.describe()
    print(details)
    # capture and produce data
    df = pd.read_csv(os.path.join(data_dir,"chicago_crime_dataset.csv"))
    partition_key = "iucr_code"
    df =df[:100]
    while args.put_records:
        kinesis_obj.put_record(df.to_json(),partition_key)








