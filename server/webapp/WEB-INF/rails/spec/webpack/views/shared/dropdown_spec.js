/*
 * Copyright 2018 ThoughtWorks, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

const m             = require('mithril');
const Stream        = require('mithril/stream');
const simulateEvent = require('simulate-event');
const Dropdown      = require('views/shared/dropdown');

describe('Dropdown widget', () => {

  const model = {
    selectedAnimal: Stream("cat"),
    animals:        [{
      id:   "cat",
      text: "Cat"
    },
      {
        id:   "dog",
        text: "Dog"
      },
      {
        id:   "rabbit",
        text: "Rabbit"
      }
    ]
  };


  const mount = () => {
    m.mount(root,
      {
        view() {
          return <div>
            <span class="other-node">Some text</span>
            <Dropdown model={model}
                      label="Select an item:"
                      attrName="selectedAnimal"
                      items={model.animals}/>;
          </div>;
        }
      }
    );
    m.redraw();
  };

  const unmount = () => {
    m.mount(root, null);
    m.redraw();
  };

  let $root, root;

  beforeEach(() => {
    [$root, root] = window.createDomElementForTest();
    mount();
  });

  afterEach(() => {
    unmount();
    window.destroyDomElementForTest();
  });

  it('should open dropdown on click', () => {
    expect($root.find('.c-dropdown')).not.toHaveClass('open');
    simulateEvent.simulate($root.find('.c-dropdown_head').get(0), 'click');
    expect($root.find('.c-dropdown')).toHaveClass('open');
  });

  it('should close dropdown when an item is selected', () => {
    simulateEvent.simulate($root.find('.c-dropdown_head').get(0), 'click');
    expect($root.find('.c-dropdown')).toHaveClass('open');
    simulateEvent.simulate($root.find('.c-dropdown_item').get(0), 'click');
    expect($root.find('.c-dropdown')).not.toHaveClass('open');
  });

  it('should open drowndown when down-arrow is clicked', () => {
    expect($root.find('.c-dropdown')).not.toHaveClass('open');
    simulateEvent.simulate($root.find('.c-down-arrow').get(0), 'click');
    expect($root.find('.c-dropdown')).toHaveClass('open');
  });

});
