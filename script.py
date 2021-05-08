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

    def __eq__(self, other):
        return self.name == other.name and self.rate == other.rate and self.quantity == other.quantity

    def __hash__(self):
        return hash(('name', self.name, 'rate', self.rate, 'quantity', self.quantity))


DIR = "rasandummy-json-data/*.json"
# Creation of an array to store the values of json.
productArray = []
# Read all file from productArray directory
for file in glob.glob(DIR):

    # Open the file
    fin = open(file, "rt")

    # Read content of the file
    content = fin.read()

    # Replace all the values
    content = content.replace('Name', 'name')
    content = content.replace('URL', 'image')
    content = content.replace('Price', 'rate')
    content = content.replace('size', 'quantity')
    content = content.replace('Size', 'quantity')
    content = content.replace('Title', 'name')
    content = content.replace('Image', 'image')
    content = content.replace('_30jeq3', 'rate')
    content = content.replace('_3i9_wc', 'mrp')
    content = content.replace('_1mbxne', 'quantity')
    content = content.replace('quatity', 'quantity')
    content = content.replace('qty', 'quantity')
    content = content.replace('\u20b9', '')

    # Convert json array to product array
    x = json.loads(content, object_hook=lambda d: SimpleNamespace(**d))
    for i in x:
        isQuantityPresent = hasattr(i, "quantity")
        isRatePresent = hasattr(i, "rate")
        isMrpPresent = hasattr(i, "mrp")
        isNamePresent = hasattr(i, "name")

        if not isNamePresent:
            setattr(i, "name", "")

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

        if i.rate != "0":
            productArray.append(Product(i.name, i.rate, i.image, i.quantity, i.mrp))

# for i in productArray:
#     print("Image: " + i.image)

duplicateCount = 0
for i in productArray:
    for j in productArray:
        if i == j and i.image == '':
            duplicateCount = duplicateCount + 1
            print(".")
            setattr(i, "image", j.image)
#
# print("duplicateCount >>>> ")
# print(duplicateCount)

# Remove duplicate products by converting list to set and back to list.
print(len(productArray))
uniqueProductsSet = set(productArray)
uniqueProductsList = list(uniqueProductsSet)
print(len(uniqueProductsList))

# Sort the list and convert to json data
uniqueProductsList.sort(key=lambda x: x.name, reverse=False)
sortedJson = json.dumps(uniqueProductsList, default=lambda x: x.__dict__)

# Write data to file
f = open("product.json", "w")
productsJson = '{"products":' + sortedJson + '}'
# print(productsJson)
f.write(productsJson)
