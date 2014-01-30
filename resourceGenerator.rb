File.open("temp.json", "w") { |io| 
  io.write("\t\t//AUTO-GENERATED JSON\n")
  Dir.glob("res/map_anim/ike_map_*.png") { |file|  
    io.write("\t\t{\n")
    io.write("\t\t\t\"name\":\t\t\"#{File.basename(file, ".*")}\",\n")
    io.write("\t\t\t\"path\":\t\t\"#{file}\"\n")
    io.write("\t\t},\n")
  }
}
