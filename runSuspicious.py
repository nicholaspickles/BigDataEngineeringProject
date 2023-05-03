import os
import subprocess
import argparse
from tqdm import tqdm

parser = argparse.ArgumentParser(description="Run sbt and scoverage test")
parser.add_argument('--directory', dest='dirct', action='store')

args = parser.parse_args()

## get into the path of the main directory where getSuspicious is
## We could potentially add arguments to move Ryland's Titian generated correct/incorrect
## files to Jon's project - These arguments might just be directories to the 2 projects'
## local location
os.chdir(args.dirct)

# subprocess.run(['ls', '-l'])
commands = ['clean', 'reload', 'coverage', 'test', 'coverageReport']

## We don't really need tqdm - it's just for visuals
print("Running sbt coverage commands...")
for i in tqdm(range(len(commands))):
    print("Running: sbt " + commands[i])
    subprocess.run(['sbt', commands[i]])

print("Getting suspicious score...")
subprocess.run(['sbt', 'run', 'getSuspicious'])
