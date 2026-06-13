import urllib.request
import ssl
import json
import re

ssl_context = ssl._create_unverified_context()
url = "https://www.timas.com.tr/kitaplar-ve-setler?q=Gargantis"

try:
    req = urllib.request.Request(
        url,
        headers={'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36'}
    )
    with urllib.request.urlopen(req, timeout=10, context=ssl_context) as response:
        html = response.read().decode('utf-8')
        m = re.search(r'<script id="__NEXT_DATA__" type="application/json">(.*?)</script>', html)
        if m:
            data = json.loads(m.group(1))
            props = data.get("props", {}).get("pageProps", {})
            
            # Print all products in props
            products = []
            def extract_products(x):
                if isinstance(x, dict):
                    if "title" in x and "image" in x and isinstance(x["image"], dict) and "src" in x["image"]:
                        products.append(x)
                    for val in x.values():
                        extract_products(val)
                elif isinstance(x, list):
                    for item in x:
                        extract_products(item)
            
            extract_products(props)
            print(f"Total products found: {len(products)}")
            for p in products:
                print(f"  - Title: {p.get('title')}, image: {p['image']['src']}")
except Exception as e:
    print("Error:", e)
