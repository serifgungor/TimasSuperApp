import urllib.request
import ssl
import json
import re

ssl_context = ssl._create_unverified_context()
url = "https://www.timas.com.tr/kitaplar-ve-setler?q=Malamander"

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
            
            # Recursive printer for items containing "Malamander"
            def search_and_print(x, path=""):
                if isinstance(x, dict):
                    # Check if Malamander is in any value
                    for k, v in x.items():
                        current_path = f"{path}['{k}']"
                        if isinstance(v, str) and "Malamander" in v:
                            print(f"Match at {current_path}: {v}")
                            # Print the whole dictionary
                            print("Context dictionary:")
                            # Clean print (max 1000 chars)
                            ctx_str = json.dumps(x, indent=2, ensure_ascii=False)
                            print(ctx_str[:1500])
                            print("-" * 50)
                        else:
                            search_and_print(v, current_path)
                elif isinstance(x, list):
                    for idx, item in enumerate(x):
                        search_and_print(item, f"{path}[{idx}]")
            
            search_and_print(props, "pageProps")
except Exception as e:
    print("Error:", e)
