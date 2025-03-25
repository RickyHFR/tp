document.addEventListener('DOMContentLoaded', function() {
  var expandableItems = document.querySelectorAll('.left-nav .expandable > span');
  expandableItems.forEach(function(item) {
    item.addEventListener('click', function() {
      this.parentElement.classList.toggle('active');
    });
  });
});