import urllib.request
import ssl
import re

ssl_context = ssl._create_unverified_context()
base_url = "https://www.timas.com.tr"

js_files = [
    "/_next/static/chunks/main-722078b1c2cac53bac64.js",
    "/_next/static/chunks/webpack-76ead5cb2544da640e18.js",
    "/_next/static/chunks/framework.950d11036ff57cb0dbbc.js",
    "/_next/static/chunks/ce474eb39a377e97b81b406b5653394b88a96ef1.51be69e6b646e21bc984.js",
    "/_next/static/chunks/pages/_app-401594dfc4d4393db0fe.js",
    "/_next/static/chunks/2aedf272c493be256484183375624fab2c795958.d781aa02878f381cce65.js",
    "/_next/static/chunks/a72cc50a61e5ff57e49683018aa2f32246246212.e212c92408cb61846064.js",
    "/_next/static/chunks/6a72ce54752610a9f20f19130c9f694f09d3a365.d92128375b23c01ba9b0.js",
    "/_next/static/chunks/pages/index-dfb96284ceb17a70c42c.js"
]

for js_path in js_files:
    url = base_url + js_path
    print(f"Downloading {url}...")
    try:
        req = urllib.request.Request(
            url,
            headers={'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36'}
        )
        with urllib.request.urlopen(req, timeout=10, context=ssl_context) as response:
            content = response.read().decode('utf-8')
            # Look for keywords like search, /arama, /kitaplar-ve-setler, etc.
            matches = re.findall(r'(\w+[\/\w\-]*\?q=|\w+[\/\w\-]*\?search=|\w+[\/\w\-]*\?text=)', content)
            if matches:
                print(f"Found matches in {js_path}: {set(matches)}")
            
            # Let's search for search-area-textfield
            if "search-area" in content:
                print(f"-> Found 'search-area' in {js_path}!")
                # Print some surrounding text
                pos = content.find("search-area")
                print("Surrounding text:", content[max(0, pos-200):pos+300])
    except Exception as e:
        print(f"Error {js_path}: {e}")
