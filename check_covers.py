import re
import urllib.request

def check_urls():
    with open("app/src/main/java/com/timas/superapp/components/Library3D.kt", "r") as f:
        content = f.read()
    
    urls = re.findall(r'coverUrl\s*=\s*"([^"]+)"', content)
    print(f"Found {len(urls)} cover URLs. Verifying...")
    
    headers = {'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64)'}
    for url in urls:
        try:
            req = urllib.request.Request(url, headers=headers, method='HEAD')
            with urllib.request.urlopen(req, timeout=5) as response:
                status = response.status
            if status != 200:
                print(f"[FAIL] {url} returned status {status}")
            else:
                print(f"[OK] {url}")
        except Exception as e:
            print(f"[ERROR] {url} failed with error: {e}")

if __name__ == "__main__":
    check_urls()
