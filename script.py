import glob
import json
from types import SimpleNamespace


class Product:
    name = "",
    rate = "",
    mrp = "",
    image = "",
    quantity = ""

    # Using of function form SimpleNamespace class to create objects
    def __init__(self, name, rate, image, quantity, mrp):
        self.name = name
        self.rate = rate
        self.image = image
        self.mrp = mrp
        self.quantity = quantity

    def toJSON(self):
        return json.dumps(self, default=lambda o: o.__dict__,
                          sort_keys=True, indent=4)


DIR = "/Users/ritesh/Downloads/rasandummy-json-data/*.json"
# Creation of an array to store the values of json.
productArray = []
# Read all file from productArray directory
for file in glob.glob(DIR):

    # Open the file
    fin = open(file, "rt")

    # Read content of the file
    content = fin.read()

    # Replace all the values
    content = content.replace('Title', 'name')
    content = content.replace('Image', 'image')
    content = content.replace('_30jeq3', 'rate')
    content = content.replace('_3i9_wc', 'mrp')
    content = content.replace('_1mbxne', 'quantity')
    content = content.replace('quatity', 'quantity')
    content = content.replace('qty', 'quantity')

    # Convert json array to product array
    x = json.loads(content, object_hook=lambda d: SimpleNamespace(**d))
    for i in x:
        # if i.mrp == None:
        isQuantityPresent = hasattr(i, "quantity")
        isRatePresent = hasattr(i, "rate")
        isMrpPresent = hasattr(i, "mrp")

        if not isMrpPresent:
            setattr(i, "mrp", "")

        if not isQuantityPresent:
            setattr(i, "quantity", "")

        if not isRatePresent:
            setattr(i, "rate", "")

        # if rate is blank and mrp is blank set rate = 0
        if i.rate == '' and i.mrp == '':
            setattr(i, "rate", "0")

        # if rate is blank and mrp is not blank set rate = mrp
        if i.rate == '' and i.mrp != '':
            setattr(i, "rate", i.mrp)

        # if rate is not blank and mrp is not blank set rate = mrp
        if i.rate != '' and i.mrp != '':
            setattr(i, "rate", i.mrp)

        productArray.append(Product(i.name, i.rate, i.image, i.quantity, i.mrp))

print(len(productArray))

finalJson = json.dumps(productArray, default=lambda x: x.__dict__)
print(finalJson)
f = open("product.Json", "w")
f.write(finalJson)
