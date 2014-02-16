f1 = File.open(ARGV.shift)
f2 = File.open(ARGV.shift)

f1.each.zip(f2.each).each do |line1, line2|
  if line1.start_with? '#'
    puts line1
    next
  end
  line1.chomp!
  line2.chomp!
  if line1 != line2
    puts line1
    puts line2
    puts "\n"
  end
end