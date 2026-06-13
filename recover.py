import json
import os

log_path = "/Users/eyupyasardemir/.gemini/antigravity/brain/be345d24-fcb0-4bed-8e32-72e54ccf656c/.system_generated/logs/transcript.jsonl"
target_path = "/Users/eyupyasardemir/Desktop/timas/TimasSuperApp/app/src/main/java/com/timas/superapp/components/Library3D.kt"

recovered_code = None

with open(log_path, "r", encoding="utf-8") as f:
    for line in f:
        if "Library3D.kt" in line and "write_to_file" in line:
            try:
                data = json.loads(line)
                tool_calls = data.get("tool_calls", [])
                for call in tool_calls:
                    if call.get("name") == "write_to_file" and "Library3D.kt" in call["args"].get("TargetFile", ""):
                        recovered_code = call["args"]["CodeContent"]
            except Exception as e:
                pass

if recovered_code:
    print("Length of raw recovered code:", len(recovered_code))
    print("Starts with:", repr(recovered_code[:20]))
    print("Ends with:", repr(recovered_code[-20:]))
    
    # Try parsing as JSON string directly
    try:
        # If it is serialized as a JSON string, we load it
        parsed = json.loads(recovered_code)
        if isinstance(parsed, str):
            recovered_code = parsed
            print("Parsed successfully via json.loads")
    except Exception as e:
        print("json.loads failed:", e)
        # Manual fallback
        if recovered_code.startswith('"') and recovered_code.endswith('"'):
            recovered_code = recovered_code[1:-1]
        recovered_code = recovered_code.replace("\\n", "\n").replace('\\"', '"').replace("\\t", "\t")
        print("Completed manual replacement fallback")
        
    with open(target_path, "w", encoding="utf-8") as out:
        out.write(recovered_code)
    print("Successfully recovered Library3D.kt!")
else:
    print("Could not find recovery content.")
