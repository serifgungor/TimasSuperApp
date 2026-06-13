import urllib.request
import ssl
import json
import re

ssl_context = ssl._create_unverified_context()
base_url = "https://www.timas.com.tr/kitaplar-ve-setler"
params = ["q", "search", "text", "query", "keyword", "filter", "searchByText"]

def check_results_for_param(param):
    url = f"{base_url}?{param}=Malamander"
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
                # Next.js pageProps contains the data. Let's dump the keys of pageProps
                props = data.get("props", {}).get("pageProps", {})
                
                # Check for product list in props
                # Often it's in props['products'] or props['data']['products'] etc.
                # Let's search recursively for list of products
                found_products = []
                def search_dict(d):
                    if isinstance(d, dict):
                        for k, v in d.items():
                            if k == "title" and isinstance(v, str) and "Malamander" in v:
                                # Found a product with Malamander!
                                found_products.append(d)
                            else:
                                search_dict(v)
                    elif isinstance(d, list):
                        for item in d:
                            search_dict(item)
                
                search_dict(props)
                if found_products:
                    print(f"Param '{param}': Found {len(found_products)} occurrences of 'Malamander' in data props!")
                    for p in found_products[:3]:
                        print("  Product:", p.get("title"), p.get("barcode"), p.get("isbn"))
                    return True
                else:
                    print(f"Param '{param}': No products found with 'Malamander' in title.")
    except Exception as e:
        print(f"Param '{param}' Error: {e}")
    return False

for p in params:
    if check_results_for_param(p):
        print(f"-> Found correct param: {p}")
        break
