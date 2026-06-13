import urllib.request
import ssl
import json
import re

ssl_context = ssl._create_unverified_context()
base_url = "https://www.timas.com.tr/kitaplar-ve-setler"

params = ["q", "search", "text", "query", "keyword", "filter"]

for param in params:
    url = f"{base_url}?{param}=Malamander"
    print(f"Testing URL: {url}")
    try:
        req = urllib.request.Request(
            url,
            headers={'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36'}
        )
        with urllib.request.urlopen(req, timeout=10, context=ssl_context) as response:
            html = response.read().decode('utf-8')
            print(f"  Response size: {len(html)}")
            if "Malamander" in html:
                print(f"  -> SUCCESS! Found 'Malamander' in html for {param}")
                # Let's extract the product info or check if there is __NEXT_DATA__
                m = re.search(r'<script id="__NEXT_DATA__" type="application/json">(.*?)</script>', html)
                if m:
                    print("  -> Found __NEXT_DATA__!")
                    data = json.loads(m.group(1))
                    # Let's see if we can find Malamander in the query/props
                    print("  Query:", data.get("query"))
            else:
                print(f"  -> 'Malamander' not found in response.")
    except Exception as e:
        print(f"  Error: {e}")
