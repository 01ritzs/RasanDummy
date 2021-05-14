import glob
import json
from types import SimpleNamespace


class Category:
    title = "",
    products = []

    # Using of function form SimpleNamespace class to create objects
    def __init__(self, title, products):
        self.title = title
        self.products = products

    def toJSON(self):
        return json.dumps(self, default=lambda o: o.__dict__,
                          sort_keys=True, indent=4)

    def __eq__(self, other):
        return self.title == other.title

    def __hash__(self):
        return hash(('title', self.title))


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

categoryArray = []
# Read all file from productArray directory
for file in glob.glob(DIR):
    print("[" + file.title() + "] Processing ...")
    productArray = []
    # Open the file
    fin = open(file, "rt")

    # Read content of the file
    content = fin.read()

    # Replace all the values
    content = content.replace('Title', 'mrp')
    content = content.replace('Image', 'image')
    content = content.replace('Name', 'name')
    content = content.replace('size', 'quantity')
    content = content.replace('Price', 'rate')
    content = content.replace('\u20b9', '')

    # Convert json array to product array
    x = json.loads(content, object_hook=lambda d: SimpleNamespace(**d))
    for i in x:
        isQuantityPresent = hasattr(i, "quantity")
        isRatePresent = hasattr(i, "rate")
        isMrpPresent = hasattr(i, "mrp")

        if not isMrpPresent:
            setattr(i, "mrp", "")

        if not isRatePresent:
            setattr(i, "rate", i.mrp)

        if i.rate == '':
            setattr(i, "rate", i.mrp)

        productArray.append(Product(i.name, i.rate, i.image, i.quantity, i.mrp))

    fullPath = file.title()
    category = fullPath.replace('Rasandummy-Json-Data/', '')
    category = category.replace('.Json', '')
    print("[" + file.title() + "] " + str(len(productArray)))
    uniqueProductsSet = set(productArray)
    uniqueProductsList = list(uniqueProductsSet)
    print("[" + file.title() + "] " + str(len(uniqueProductsList)))
    uniqueProductsList.sort(key=lambda x: x.name, reverse=False)
    categoryArray.append(Category(category, uniqueProductsList))

# Remove duplicate products by converting list to set and back to list.
print("[Category] " + str(len(categoryArray)))

# Sort the list and convert to json data
sortedJson = json.dumps(categoryArray, default=lambda x: x.__dict__)

# Write data to file
f = open("rasanDummy.json", "w")
finalJson = '{"categories":' + sortedJson + '}'
f.write(finalJson)
