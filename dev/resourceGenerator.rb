File.open("temp.json", "w") { |io| 
  Dir.glob("res/battle_anim/static/*.png") { |file|  
    puts File.basename(file, ".*")
    io.write("\t\t{\n")
    io.write("\t\t\t\"name\":\t\t\"#{File.basename(file, ".*")}\",\n")
    io.write("\t\t\t\"path\":\t\t\"#{file}\"\n")
    io.write("\t\t},\n")
  }
}
