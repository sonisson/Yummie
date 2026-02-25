import json

# Đọc provinces.json
with open("provinces.json", "r", encoding="utf-8") as f:
    provinces = json.load(f)

# Tạo danh sách mới chỉ giữ lại name_with_type
provinces_simplified = [
    {"name_with_type": p["name_with_type"]} for p in provinces
]

# Lưu ra file mới
with open("provinces_simplified.json", "w", encoding="utf-8") as f:
    json.dump(provinces_simplified, f, ensure_ascii=False, indent=2)

print("Đã lưu provinces_simplified.json")