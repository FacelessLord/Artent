from os import listdir
from os.path import isfile, join
from os import mkdir


files = list(sorted([f[5:-4] for f in listdir(".") if isfile(join(".", f)) if f.startswith("part_")]))

print(files)


with open("model_base.json", 'r') as f:
    model_text = f.readlines()

i=1
for file in files:
    with open("models/"+file+'.json', 'w') as f:
        for line in model_text:
            f.write(line.replace("model_base", file))
    
    
    print(f'"type={i}": {{ "model": "aam:block/circles/{file}" }},')
    i+=1

